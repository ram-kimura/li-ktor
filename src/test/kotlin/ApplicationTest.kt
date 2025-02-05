import com.example.domain.Priority
import com.example.domain.Task
import com.example.util.jdbi
import com.example.util.jsonSerializer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import testing.withCustomApplicationTest
import java.util.*

class ApplicationTest {
    @Test
    fun testRoot() = withCustomApplicationTest {
        val response = client.get("/")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("Hello Ktor")
    }

    @Test
    fun testBusy() = withCustomApplicationTest {
        val response = client.get("/busy-test") // todo: refactor to non string path

        assertThat(response.status).isEqualTo(HttpStatusCode.ServiceUnavailable)
        assertThat(response.bodyAsText()).isEqualTo("App in illegal state as Too Busy")
    }

    @Test
    fun getAll() = withCustomApplicationTest {
        val client = createClient {
            this.install(ContentNegotiation) {
                json(jsonSerializer)
            }
        }

        val response = client.get("/tasks")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isNotNull()
    }

    @Test
    fun getByPriority() = withCustomApplicationTest {
        val client = createClient {
            this.install(ContentNegotiation) {
                json(jsonSerializer)
            }
        }

        val targetPriority = Priority.HIGH

        val response = client.get("/tasks/priority/$targetPriority")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("[]")
    }

    @Test
    fun registerTask() = withCustomApplicationTest {
        val client = createClient {
            this.install(ContentNegotiation) {
                json(jsonSerializer)
            }
        }

        val task = Task(
            "tenant",
            UUID.randomUUID(),
            "New task",
            Priority.LOW
        )
        val response = client.post("/tasks") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(task)
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
    }

    @Test
    fun deleteTask() = withCustomApplicationTest {
        val client = createClient {
            this.install(ContentNegotiation) {
                json(jsonSerializer)
            }
        }

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
