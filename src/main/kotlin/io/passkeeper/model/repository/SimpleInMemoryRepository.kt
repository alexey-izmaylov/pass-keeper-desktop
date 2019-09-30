package io.passkeeper.model.repository

import io.passkeeper.model.entity.Password
import java.util.*
import kotlin.collections.HashMap

class SimpleInMemoryRepository : SecretRepository {
    private val map: MutableMap<String, Password> = HashMap()

    override fun list(): List<Password> = Collections.unmodifiableList(map.values.toList())

    override fun get(domain: String) = map[domain]

    override fun createOrUpdate(password: Password): Password {
        map[password.domain] = password
        return password
    }

    override fun delete(domain: String) {
        map.remove(domain)
    }
}