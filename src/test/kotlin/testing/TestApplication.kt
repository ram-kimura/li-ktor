package testing

import com.example.module
import com.example.util.jsonSerializer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

inline fun withCustomApplicationTest(crossinline test: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application {
            module()
        }
        val client = createClient {
            this.install(ContentNegotiation) {
                json(jsonSerializer)
            }
        }

        test()
    }
}
