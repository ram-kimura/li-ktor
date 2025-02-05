package domain

import com.example.domain.Priority
import com.example.domain.Task

class TaskTest

fun createTask() = Task.of(
    tenantNameID = "tenant",
    title = "test title",
    priority = Priority.MEDIUM
)
