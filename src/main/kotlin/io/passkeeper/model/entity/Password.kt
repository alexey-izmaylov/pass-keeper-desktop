package io.passkeeper.model.entity

class Password(
        val domain: String,
        val username: String = "",
        val password: String = "",
        val notes: String = ""
)

fun password(): Password = Password(
        domain = "-new-"
)

fun password(secret: Secret): Password = Password(
        domain = secret.domain,
        username = secret.username,
        password = secret.password
)

fun secret(password: Password): Secret = Secret(
        domain = password.domain,
        type = "password",
        username = password.username,
        password = password.password
)