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
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import java.security.MessageDigest
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory

sealed interface XTrackLoginResult {
    data class Success(val user: XTrackUser) : XTrackLoginResult
    data class Error(val message: String) : XTrackLoginResult
}

data class XTrackUser(
    val id: String,
    val login: String,
    val name: String,
    val departmentId: String,
    val departmentName: String
)

object xtrack_manager {

    private const val GET_USER_PAYLOAD = """
        <msg>
            <command>GetUser</command>
            <terminal>ERP</terminal>
        </msg>
    """

    private const val GET_DEPARTMENT_PAYLOAD = """
        <msg>
            <command>GetDepartment</command>
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
                row["LOGIN"]?.uppercase(Locale.ROOT) == normalizedLogin &&
                    row["PASSWORD"]?.uppercase(Locale.ROOT) == hashedPassword
            } ?: return@runCatching XTrackLoginResult.Error("Usuário ou senha inválidos")

            if (matched["ACTIVE"]?.trim() == "0") {
                return@runCatching XTrackLoginResult.Error("Usuário inativo")
            }

            val departmentId = matched["DEPARTMENT"].orEmpty()
            val departmentName = resolveDepartmentName(url, departmentId)

            val user = XTrackUser(
                id = matched["ID"].orEmpty(),
                login = matched["LOGIN"].orEmpty(),
                name = matched["NAME"].orEmpty(),
                departmentId = departmentId,
                departmentName = departmentName
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

    private fun resolveDepartmentName(xtrackUrl: String, departmentId: String): String {
        if (departmentId.isBlank()) return "-"
        val departmentsXml = postXml(xtrackUrl, GET_DEPARTMENT_PAYLOAD)
        val departments = parseDataRows(departmentsXml)
        return departments.firstOrNull { it["ID"] == departmentId }
            ?.get("NAME")
            ?.ifBlank { departmentId }
            ?: departmentId
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

    private fun parseDataRows(xml: String): List<Map<String, String>> {
        if (xml.isBlank()) return emptyList()

        val factory = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = false
            isXIncludeAware = false
            runCatching { setFeature("http://apache.org/xml/features/disallow-doctype-decl", true) }
            runCatching { setFeature("http://xml.org/sax/features/external-general-entities", false) }
            runCatching { setFeature("http://xml.org/sax/features/external-parameter-entities", false) }
        }

        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(InputSource(StringReader(xml)))
        val rows = doc.getElementsByTagName("data")
        val result = mutableListOf<Map<String, String>>()

        for (i in 0 until rows.length) {
            val node = rows.item(i) as? Element ?: continue
            val row = linkedMapOf<String, String>()
            val children = node.childNodes
            for (j in 0 until children.length) {
                val child = children.item(j) as? Element ?: continue
                row[child.tagName.uppercase(Locale.ROOT)] = child.textContent?.trim().orEmpty()
            }
            if (row.isNotEmpty()) {
                result.add(row)
            }
        }
        return result
    }

    private fun md5Upper(value: String): String {
        val bytes = MessageDigest.getInstance("MD5")
            .digest(value.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02X".format(it) }
    }
}
