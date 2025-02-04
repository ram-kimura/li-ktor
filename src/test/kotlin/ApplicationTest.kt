import com.example.domain.Priority
import com.example.domain.Task
import com.example.module
import com.example.util.jsonSerializer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        val response = client.get("/")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("Hello Ktor")
    }

    @Test
    fun getAll() = testApplication {
        application {
            module()
        }
        val client = createClient {
            this.install(ContentNegotiation) {
                json(jsonSerializer)
            }
        }

        val response = client.get("/tasks")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(response.bodyAsText()).isEqualTo("[]")
    }

    @Test
    fun getByPriority() = testApplication {
        application {
            module()
        }
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
    fun registerTask() = testApplication {
        application {
            module()
        }
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
}
