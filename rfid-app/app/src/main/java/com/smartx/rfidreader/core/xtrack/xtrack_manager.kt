package com.smartx.rfidreader.core.xtrack

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest
import java.util.Locale
import java.util.concurrent.TimeUnit

sealed interface XTrackLoginResult {
    data class Success(val user: XTrackUser) : XTrackLoginResult
    data class Error(val message: String) : XTrackLoginResult
}

data class XTrackUser(
    val id: String,
    val login: String,
    val name: String,
    val allowedDepartmentIds: List<String>
)

data class XTrackLocation(
    val id: String,
    val name: String
)

data class XTrackObjectInfo(
    val description: String,
    val objectIdCode: String
)

private data class ParsedRow(
    val fields: Map<String, List<String>>
) {
    fun first(name: String): String = fields[name.uppercase(Locale.ROOT)]?.firstOrNull().orEmpty()
    fun all(name: String): List<String> = fields[name.uppercase(Locale.ROOT)].orEmpty()
}

object xtrack_manager {

    private const val TAG = "xtrack_manager"

    private const val GET_USER_PAYLOAD = "<msg><command>GetUser</command><terminal>ERP</terminal></msg>"

    private const val GET_LOCATION_PAYLOAD = "<msg><command>GetLocation</command><terminal>ERP</terminal></msg>"

    private const val GET_OBJECT_COMMAND = "GetObject"
    private const val MOVE_LOCATION_COMMAND = "MoveLocation"
    private const val MOVE_LOCATION_TERMINAL = "SAPext"

    private const val RESPONSE_PREVIEW_MAX = 500
    private const val NETWORK_TIMEOUT_SECONDS = 5L

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .callTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val _currentUser = MutableStateFlow<XTrackUser?>(null)
    val currentUser: StateFlow<XTrackUser?> = _currentUser.asStateFlow()

    suspend fun login(
        xtrackUrl: String,
        username: String,
        password: String
    ): XTrackLoginResult = withContext(Dispatchers.IO) {
        val url = xtrackUrl.trim()
        if (url.isBlank()) {
            return@withContext XTrackLoginResult.Error("xtrack_url não configurada")
        }

        val normalizedLogin = username.trim().uppercase(Locale.ROOT)
        val normalizedPassword = password.trim().uppercase(Locale.ROOT)
        if (normalizedLogin.isBlank() || normalizedPassword.isBlank()) {
            return@withContext XTrackLoginResult.Error("Usuário e senha são obrigatórios")
        }

        return@withContext runCatching {
            val usersXml = postXml(url, GET_USER_PAYLOAD)
            val users = parseDataRows(usersXml)

            val hashedPassword = md5Upper(normalizedPassword)
            val matched = users.firstOrNull { row ->
                row.first("LOGIN").uppercase(Locale.ROOT) == normalizedLogin &&
                    row.first("PASSWORD").uppercase(Locale.ROOT) == hashedPassword
            } ?: return@runCatching XTrackLoginResult.Error("Usuário ou senha inválidos")

            if (matched.first("ACTIVE").trim() == "0") {
                return@runCatching XTrackLoginResult.Error("Usuário inativo")
            }

            val allowedDepartmentIds = matched.all("DEPARTMENT")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()

            val user = XTrackUser(
                id = matched.first("ID"),
                login = matched.first("LOGIN"),
                name = matched.first("NAME"),
                allowedDepartmentIds = allowedDepartmentIds
            )
            _currentUser.value = user
            XTrackLoginResult.Success(user)
        }.getOrElse { e ->
            XTrackLoginResult.Error("Falha no login XTrack: ${e.message ?: "erro inesperado"}")
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    suspend fun checkCommunication(xtrackUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        val url = xtrackUrl.trim()
        if (url.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("xtrack_url não configurada"))
        }

        runCatching {
            Log.i(TAG, "CheckCommunication -> $url")
            val xml = postXml(url, GET_LOCATION_PAYLOAD)
            if (xml.isBlank()) {
                throw IllegalStateException("XTrack respondeu vazio (sem conteúdo XML)")
            }

            val rows = parseDataRows(xml)
            if (rows.isEmpty()) {
                throw IllegalStateException(
                    "XTrack respondeu, porém sem nós <data> reconhecíveis. Preview: ${xml.preview()}"
                )
            }

            Unit
        }.recoverCatching { err ->
            val reason = when (err) {
                is UnknownHostException -> "Sem internet ou DNS indisponível (${err.message.orEmpty()})"
                is SocketTimeoutException -> "Timeout ao comunicar com XTrack"
                is ConnectException -> "Falha de conexão com o servidor XTrack (${err.message.orEmpty()})"
                is IOException -> "Erro de rede ao comunicar com XTrack (${err.message.orEmpty()})"
                else -> err.message ?: "Erro inesperado ao comunicar com XTrack"
            }
            throw IllegalStateException(reason, err)
        }
    }

    suspend fun fetchLocations(xtrackUrl: String): Result<List<XTrackLocation>> = withContext(Dispatchers.IO) {
        val url = xtrackUrl.trim()
        if (url.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("xtrack_url não configurada"))
        }

        runCatching {
            val locations = requestLocations(url)
            Log.i(TAG, "GetLocation success: ${locations.size} localização(ões) recebida(s)")
            locations
        }.onFailure { err ->
            Log.e(TAG, "GetLocation failed: ${err.message}", err)
        }
    }

    suspend fun fetchObjectInfos(
        xtrackUrl: String,
        epcs: List<String>
    ): Result<Map<String, XTrackObjectInfo>> = withContext(Dispatchers.IO) {
        val url = xtrackUrl.trim()
        if (url.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("xtrack_url não configurada"))
        }

        val normalizedEpcs = epcs
            .map { it.trim().uppercase(Locale.ROOT) }
            .filter { it.isNotBlank() }
            .distinct()

        if (normalizedEpcs.isEmpty()) {
            return@withContext Result.success(emptyMap())
        }

        runCatching {
            val payload = buildGetObjectPayload(normalizedEpcs)
            Log.i(TAG, "POST GetObject -> $url (epcs=${normalizedEpcs.size})")
            val xml = postXml(url, payload)
            if (xml.isBlank()) {
                throw IllegalStateException("XTrack retornou resposta vazia para GetObject")
            }

            val parsed = parseObjectInfosXml(xml)
            Log.i(TAG, "GetObject success: ${parsed.size} item(ns) mapeado(s)")
            parsed
        }.onFailure { err ->
            Log.e(TAG, "GetObject failed: ${err.message}", err)
        }
    }

    suspend fun moveLocation(
        xtrackUrl: String,
        objectIdCodes: List<String>,
        locationName: String
    ): Result<Int> = withContext(Dispatchers.IO) {
        val url = xtrackUrl.trim()
        if (url.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("xtrack_url não configurada"))
        }

        val normalizedLocation = locationName.trim()
        if (normalizedLocation.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Localização não definida"))
        }

        val normalizedIds = objectIdCodes
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()

        if (normalizedIds.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Nenhum objeto cadastrado para enviar"))
        }

        runCatching {
            val payload = buildMoveLocationPayload(normalizedIds, normalizedLocation)
            Log.i(TAG, "POST MoveLocation -> $url (objects=${normalizedIds.size}, location=$normalizedLocation)")
            postXml(url, payload)
            normalizedIds.size
        }.onFailure { err ->
            Log.e(TAG, "MoveLocation failed: ${err.message}", err)
        }
    }

    private fun requestLocations(url: String): List<XTrackLocation> {
        Log.i(TAG, "POST GetLocation -> $url")
        val xml = postXml(url, GET_LOCATION_PAYLOAD)
        if (xml.isBlank()) {
            throw IllegalStateException("XTrack retornou resposta vazia para GetLocation")
        }

        val rows = parseDataRows(xml)
        if (rows.isEmpty()) {
            val preview = xml.preview()
            throw IllegalStateException(
                "XTrack respondeu, mas sem nós <data> reconhecíveis em GetLocation. " +
                    "Preview: $preview"
            )
        }

        val locations = parseLocationsXml(xml)
        if (locations.isEmpty()) {
            val firstKeys = rows.firstOrNull()?.fields?.keys?.joinToString(", ").orEmpty()
            throw IllegalStateException(
                "GetLocation retornou ${rows.size} bloco(s) <data>, mas sem ID/NAME válidos. " +
                    "Campos encontrados: ${if (firstKeys.isBlank()) "nenhum" else firstKeys}"
            )
        }

        return locations
    }

    private fun postXml(url: String, payload: String): String {
        val body = payload.trimIndent()
            .toRequestBody("application/xml; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        httpClient.newCall(request).execute().use { response ->
            val rawBody = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                val contentType = response.header("Content-Type").orEmpty()
                throw IllegalStateException(
                    "HTTP ${response.code} no XTrack (contentType=$contentType). " +
                        "Payload=${payload.preview()} | Body=${rawBody.preview()}"
                )
            }
            Log.d(
                TAG,
                "HTTP ${response.code} GetLocation/GetUser contentType=${response.header("Content-Type").orEmpty()} " +
                    "bodyPreview=${rawBody.preview()}"
            )
            return rawBody
        }
    }

    internal fun parseLocationsXml(xml: String): List<XTrackLocation> {
        val rows = parseDataRows(xml)
        return rows.mapNotNull { row ->
            val id = row.first("ID").trim()
            val name = row.first("NAME").trim()
            if (id.isBlank() && name.isBlank()) return@mapNotNull null
            XTrackLocation(id = id, name = if (name.isBlank()) id else name)
        }.distinctBy { it.id.ifBlank { it.name } }
    }

    internal fun parseObjectInfosXml(xml: String): Map<String, XTrackObjectInfo> {
        if (xml.isBlank()) return emptyMap()

        val dataRegex = Regex(
            "<data\\b[^>]*>(.*?)</data>",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )
        val identificationRegex = Regex(
            "<IDENTIFICATION\\b[^>]*>(.*?)</IDENTIFICATION>",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )
        val fieldRegex = Regex(
            "<([A-Za-z0-9_]+(?::[A-Za-z0-9_]+)?)\\b[^>]*>(.*?)</\\1>",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )

        val result = linkedMapOf<String, XTrackObjectInfo>()
        for (dataMatch in dataRegex.findAll(xml)) {
            val block = dataMatch.groupValues[1]
            val description = extractFirstValue(block, "DESCRIPTION")
            val objectIdCode = extractFirstValue(block, "IDCODE")

            for (idMatch in identificationRegex.findAll(block)) {
                val idBlock = idMatch.groupValues[1]
                var type = ""
                var idCode = ""

                for (field in fieldRegex.findAll(idBlock)) {
                    val key = field.groupValues[1].substringAfter(':').uppercase(Locale.ROOT)
                    val value = xmlUnescape(field.groupValues[2].trim())
                    when (key) {
                        "TYPE" -> type = value
                        "IDCODE" -> idCode = value
                    }
                }

                if (type.equals("RFID", ignoreCase = true) && idCode.isNotBlank()) {
                    val epc = idCode.trim().uppercase(Locale.ROOT)
                    val desc = if (description.isBlank()) epc else description
                    val objId = objectIdCode.trim().ifBlank { epc }
                    result[epc] = XTrackObjectInfo(
                        description = desc,
                        objectIdCode = objId
                    )
                }
            }
        }

        return result
    }

    private fun parseDataRows(xml: String): List<ParsedRow> {
        if (xml.isBlank()) return emptyList()
        val result = mutableListOf<ParsedRow>()

        val dataRegex = Regex("<data\\b[^>]*>(.*?)</data>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        val fieldRegex = Regex(
            "<([A-Za-z0-9_]+(?::[A-Za-z0-9_]+)?)\\b[^>]*>(.*?)</\\1>",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )

        for (dataMatch in dataRegex.findAll(xml)) {
            val block = dataMatch.groupValues[1]
            val row = linkedMapOf<String, MutableList<String>>()
            for (field in fieldRegex.findAll(block)) {
                val rawKey = field.groupValues[1]
                val key = rawKey.substringAfter(':').uppercase(Locale.ROOT)
                val value = xmlUnescape(field.groupValues[2].trim())
                row.getOrPut(key) { mutableListOf() }.add(value)
            }
            if (row.isNotEmpty()) {
                result.add(ParsedRow(row))
            }
        }
        return result
    }

    private fun xmlUnescape(value: String): String {
        return value
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&amp;", "&")
    }

    private fun md5Upper(value: String): String {
        val bytes = MessageDigest.getInstance("MD5")
            .digest(value.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02X".format(it) }
    }

    private fun buildGetObjectPayload(epcs: List<String>): String {
        val objectsXml = epcs.joinToString(separator = "") { epc ->
            "<object><EPC>${xmlEscape(epc)}</EPC></object>"
        }
        return "<msg><command>$GET_OBJECT_COMMAND</command><terminal>ERP</terminal><data>$objectsXml</data></msg>"
    }

    private fun buildMoveLocationPayload(objectIdCodes: List<String>, locationName: String): String {
        val dataXml = objectIdCodes.joinToString(separator = "") { idCode ->
            "<data><object>${xmlEscape(idCode)}</object><location>${xmlEscape(locationName)}</location></data>"
        }
        return "<msg><command>$MOVE_LOCATION_COMMAND</command><terminal>$MOVE_LOCATION_TERMINAL</terminal>$dataXml</msg>"
    }

    private fun extractFirstValue(xmlBlock: String, tagName: String): String {
        val regex = Regex(
            "<$tagName\\b[^>]*>(.*?)</$tagName>",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )
        return regex.find(xmlBlock)
            ?.groupValues
            ?.getOrNull(1)
            ?.let { xmlUnescape(it.trim()) }
            .orEmpty()
    }

    private fun xmlEscape(value: String): String {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    private fun String.preview(max: Int = RESPONSE_PREVIEW_MAX): String {
        val normalized = replace("\n", " ").replace("\r", " ").trim()
        if (normalized.length <= max) return normalized
        return normalized.take(max) + "..."
    }
}
