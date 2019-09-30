package io.passkeeper.model.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.passkeeper.model.entity.Password
import io.passkeeper.model.entity.Secret
import io.passkeeper.model.entity.password
import io.passkeeper.model.entity.secret
import mu.KLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Duration

class HttpRestRepository : SecretRepository {
    companion object : KLogging()

    private val url = "http://85.143.11.137:9000"
    private val client = OkHttpClient.Builder().callTimeout(Duration.ofMinutes(1)).build()
    private val mapper = jacksonObjectMapper()

    override fun list(): List<Password> {
        val request = Request.Builder()
                .url("$url/secrets/")
                .get()
                .build()
        client.newCall(request).execute().use { response ->
            logger.info {
                """
                    list:
                    status: ${response.code}
                    message: ${response.message}
                """.trimIndent()
            }
            val body = response.body
            return if (body != null) {
                mapper.readValue<List<Secret>>(body.byteStream())
                        .filter { "password" == it.type }
                        .map { password(it) }
            } else emptyList()
        }
    }

    override fun get(domain: String): Password {
        val request = Request.Builder()
                .url("$url/secrets/$domain")
                .get()
                .build()
        client.newCall(request).execute().use { response ->
            logger.info {
                """
                    get:
                    status: ${response.code}
                    message: ${response.message}
                """.trimIndent()
            }
            val body = response.body
            return if (body != null) {
                password(mapper.readValue(body.byteStream()))
            } else password()
        }
    }

    override fun createOrUpdate(password: Password): Password {
        val secret = secret(password)
        val deviceId = "85.143.11.189"
        val request = Request.Builder()
                .url("$url/secrets/${secret.domain}?device_id=$deviceId")
                .header("Content-Type", "application/json")
                .put(mapper.writeValueAsString(secret).orEmpty().toRequestBody())
                .build()
        client.newCall(request).execute().use {
            logger.info {
                """
                    createOrUpdate:
                    status: ${it.code}
                    message: ${it.message}
                """.trimIndent()
            }
        }
        return password
    }

    override fun delete(domain: String) {
        val request = Request.Builder()
                .url("$url/secrets/$domain")
                .delete()
                .build()
        client.newCall(request).execute().use {
            logger.info {
                """
                    delete:
                    status: ${it.code}
                    message: ${it.message}
                """.trimIndent()
            }
        }
    }
}