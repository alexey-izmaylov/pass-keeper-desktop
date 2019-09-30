package io.passkeeper.model.repository

import io.passkeeper.model.entity.Password

interface SecretRepository {

    fun list(): List<Password>

    fun get(domain: String): Password?

    fun createOrUpdate(password: Password): Password

    fun delete(domain: String)
}