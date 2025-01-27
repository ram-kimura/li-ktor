package com.example

import com.example.domain.Priority
import com.example.domain.Task
import com.example.util.jsonSerializer
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    install(ContentNegotiation) {
        json(jsonSerializer)
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/test1") {
            val text = "<h1>Hello World!</h1>"
            val type = ContentType.parse("text/html")
            call.respondText(text, type)
        }

        get("/error-test") {
            throw IllegalArgumentException("Too Busy")
        }

        get("/tasks") {
            call.respond(
                listOf(
                    Task(UUID.randomUUID(), "Clean the house", Priority.Medium),
                )
            )
        }
    }
}
