package com.example

import com.example.controller.taskResources
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello Ktor")
        }

        get("/test1") {
            val text = "<h1>Hello World!</h1>"
            val type = ContentType.parse("text/html")
            call.respondText(text, type)
        }

        get("/error-test") {
            throw IllegalArgumentException("Too Busy")
        }

        taskResources()
    }
}
