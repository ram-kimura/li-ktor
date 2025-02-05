package domain

import com.example.domain.Priority
import com.example.domain.Task

fun createTaskFixture(
    tenantNameId: String = "tenant",
    title: String = "test title",
    priority: Priority = Priority.MEDIUM
) = Task.of(
    tenantNameID = tenantNameId,
    title = title,
    priority = priority
)
