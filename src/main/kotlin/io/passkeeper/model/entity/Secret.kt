package io.passkeeper.model.entity

import com.fasterxml.jackson.annotation.JsonAnySetter


class Secret(
        val domain: String,
        val type: String,
        val username: String,
        val password: String,
        val details: MutableMap<String, Any> = mutableMapOf()
) {
    @JsonAnySetter
    fun setDetail(key: String, value: Any) {
        details[key] = value
    }
}