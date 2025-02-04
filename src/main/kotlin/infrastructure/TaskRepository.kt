package com.example.infrastructure

import com.example.domain.Priority
import com.example.domain.Task
import com.example.util.jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import java.util.*

object TaskRepository {

    fun getAll(): List<Task> {
        val query = """
            SELECT tenant_name_id, task_uuid, title, priority
            FROM task
        """.trimIndent()

        return jdbi.withHandle<List<Task>, Exception> { handle ->
            handle.createQuery(query)
                .mapTo(Task::class.java)
                .list()
        }
    }

    fun getByUUID(taskUUID: UUID): Task? {
        val query = """
            select tenant_name_id, task_uuid, title, priority 
            from task
            where task_uuid = :taskUUID
        """.trimIndent()

        return jdbi.open().use { handle ->
            handle.createQuery(query)
                .bind("taskUUID", taskUUID)
                .mapTo(Task::class.java)
                .singleOrNull()
        }
    }

    fun getTasksBy(priority: Priority): List<Task> {
        val query = """
           select tenant_name_id, task_uuid, title, priority
           from task
           where priority = :priority
        """.trimIndent()

        return jdbi.open().use { handle ->
            handle.createQuery(query)
                .bind("priority", priority)
                .mapTo(Task::class.java)
                .list()
        }
    }

    fun register(task: Task) {
        val query = """
            insert into task (<columns>)
            values (:tenantNameID, :taskUUID, :title, :priority, now(), null)
        """.trimIndent()

        jdbi.open().use { handle ->
            handle.createUpdate(query)
                .defineList(
                    "columns",
                    listOf("tenant_name_id", "task_uuid", "title", "priority", "created_at", "updated_at")
                )
                .bindKotlin(task)
                .execute()
        }
    }

    fun remove(id: UUID): Boolean {
        return false
//        tasks.removeIf { it.id == id }
    }
}
