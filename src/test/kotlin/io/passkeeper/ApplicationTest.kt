package io.passkeeper

import io.passkeeper.model.repository.HttpRestRepository
import mu.KLogging
import org.junit.jupiter.api.Test

class ApplicationTest {
    companion object : KLogging()

    private val repository = HttpRestRepository()

    @Test
    fun test() {
        val domains = repository.list()
        domains.forEach { logger.info { "${it.domain} ${it.username} ${it.password}" } }
    }
}