package com.example.infrastructure

import com.example.domain.Priority
import com.example.domain.Task
import com.example.util.jdbi
import java.util.*

object TaskRepository {

    fun getAll(): List<Task> {
        val query = """
            SELECT task_uuid, title, priority
            FROM task
        """.trimIndent()

        return jdbi.withHandle<List<Task>, Exception> { handle ->
            handle.createQuery(query)
                .mapTo(Task::class.java)
                .list()
        }
    }

    fun getTasksBy(priority: Priority): List<Task> {
        return emptyList()
    }

    fun addTask(task: Task) {
//        tasks.add(task)
    }

    fun remove(id: UUID): Boolean {
        return false
//        tasks.removeIf { it.id == id }
    }
}
