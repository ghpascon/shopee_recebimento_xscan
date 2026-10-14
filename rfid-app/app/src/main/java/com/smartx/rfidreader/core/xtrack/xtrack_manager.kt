package com.smartx.rfidreader.core.xtrack

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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

private data class ParsedRow(
    val fields: Map<String, List<String>>
) {
    fun first(name: String): String = fields[name.uppercase(Locale.ROOT)]?.firstOrNull().orEmpty()
    fun all(name: String): List<String> = fields[name.uppercase(Locale.ROOT)].orEmpty()
}

object xtrack_manager {

    private const val GET_USER_PAYLOAD = """
        <msg>
            <command>GetUser</command>
            <terminal>ERP</terminal>
        </msg>
    """

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .callTimeout(25, TimeUnit.SECONDS)
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

    private fun postXml(url: String, payload: String): String {
        val body = payload.trimIndent()
            .toRequestBody("application/xml; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("HTTP ${response.code}")
            }
            return response.body?.string().orEmpty()
        }
    }

    private fun parseDataRows(xml: String): List<ParsedRow> {
        if (xml.isBlank()) return emptyList()
        val result = mutableListOf<ParsedRow>()

        val dataRegex = Regex("<data>(.*?)</data>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        val fieldRegex = Regex("<([A-Za-z0-9_]+)>(.*?)</\\1>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

        for (dataMatch in dataRegex.findAll(xml)) {
            val block = dataMatch.groupValues[1]
            val row = linkedMapOf<String, MutableList<String>>()
            for (field in fieldRegex.findAll(block)) {
                val key = field.groupValues[1].uppercase(Locale.ROOT)
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
}
