package testing

import com.example.module
import io.ktor.server.testing.*

inline fun withCustomApplicationTest(crossinline test: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application {
            module()
        }

        test()
    }
}
