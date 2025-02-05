package com.example.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    val tenantNameID: String,
    @Contextual
    val taskUUID: UUID,
    val title: String,
    val priority: Priority,
) {
    companion object {
        fun of(tenantNameID: String, title: String, priority: Priority) =
            Task(tenantNameID, UUID.randomUUID(), title, priority)
    }
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
}
