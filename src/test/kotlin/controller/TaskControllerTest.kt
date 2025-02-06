package controller

import com.example.domain.Priority
import com.example.util.jdbi
import domain.createTaskFixture
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import testing.withCustomApplicationTest
import java.util.*

class TaskControllerTest {
    @Test
    fun getAll() = withCustomApplicationTest {
        val response = client.get("/tasks")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isNotNull()
    }

    @Test
    fun getByPriority() = withCustomApplicationTest {
        val targetPriority = Priority.HIGH

        val response = client.get("/tasks/priority/$targetPriority")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("[]")
    }

    @Test
    fun registerTask() = withCustomApplicationTest {
        val task = createTaskFixture()
        val json = """
            {
                "tenantNameID": "${task.tenantNameID}",
                "taskUUID": "${task.taskUUID}",
                "title": "${task.title}",
                "priority": "${task.priority}"
            }
        """.trimIndent()

        val response = client.post("/tasks") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(json)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
    }

    @Test
    fun deleteTask() = withCustomApplicationTest {
        val taskId = jdbi.open().use { handle ->
            handle.createQuery(
                """
                select task_uuid
                from task
                limit 1
            """
            ).mapTo(UUID::class.java).singleOrNull()
        }

        val response = client.delete("/tasks/$taskId")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    }
}
