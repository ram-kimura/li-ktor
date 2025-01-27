package com.example.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(
    @Contextual
    val id: UUID,
    val title: String,
    val priority: Priority,
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
}
