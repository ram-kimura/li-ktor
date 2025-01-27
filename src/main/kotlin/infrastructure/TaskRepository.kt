package com.example.infrastructure

import com.example.domain.Priority
import com.example.domain.Task
import java.util.*

object TaskRepository {
    private val tasks = mutableListOf(
        Task(
            UUID.randomUUID(),
            "Low priority task",
            Priority.LOW
        ),
        Task(
            UUID.randomUUID(),
            "Medium priority task",
            Priority.MEDIUM
        ),
        Task(
            UUID.randomUUID(),
            "High priority task",
            Priority.HIGH
        )
    )

    fun getAllTasks() = tasks

    fun getTasksBy(priority: Priority) = tasks.filter { it.priority == priority }

    fun addTask(task: Task) {
        tasks.add(task)
    }
}
