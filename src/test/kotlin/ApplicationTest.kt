import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import testing.withCustomApplicationTest

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
}
