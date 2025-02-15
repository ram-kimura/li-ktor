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

        return jdbi.withHandle<_, Exception> { handle ->
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

    @Deprecated("Use bulkRegister instead")
    fun deprecatedBulkRegister(tasks: List<Task>): Int {
        val query = """
            insert into task values (:tenant_name_id, :task_uuid, :title, :priority, null, now(), null)
        """.trimIndent()

        return jdbi.inTransaction<_, Exception> { handle ->
            val batch = handle.prepareBatch(query)
            tasks.forEach { task ->
                batch.bind("tenant_name_id", task.tenantNameID)
                    .bind("task_uuid", task.taskUUID)
                    .bind("title", task.title)
                    .bind("priority", task.priority)
                    .add()
            }
            val counts = batch.execute()
            counts.size
        }
    }

    fun bulkRegister(tasks: List<Task>): Int {
        val query = """
           insert into task values <values>
        """.trimIndent()

        val taskRows = tasks.map { TaskRow.from(it) }

        return jdbi.inTransaction<_, Exception> { handle ->
            handle.createUpdate(query)
                .bindBeanList(
                    "values",
                    taskRows,
                    listOf(
                        "tenantNameID",
                        "taskUUID",
                        "title",
                        "priority",
                        "completedAt",
                        "createdAt",
                        "updatedAt"
                    )
                )
                .execute()
        }
    }

    fun remove(id: UUID) {
        val query = """
            delete from task
            where task_uuid = :id
        """.trimIndent()

        jdbi.useTransaction<Exception> { handle ->
            handle.createUpdate(query)
                .bind("id", id)
                .execute()
        }
    }

    data class TaskRow(
        val tenantNameID: String,
        val taskUUID: UUID,
        val title: String,
        val priority: Priority,
        val completedAt: Date? = null,
        val createdAt: Date = Date(),
        val updatedAt: Date? = null
    ) {
        companion object {
            fun from(task: Task) = TaskRow(
                tenantNameID = task.tenantNameID,
                taskUUID = task.taskUUID,
                title = task.title,
                priority = task.priority
            )
        }
    }
}
