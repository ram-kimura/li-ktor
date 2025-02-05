package com.example

import com.example.controller.taskResources
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello Ktor")
        }

        get("/busy-test") {
            throw IllegalArgumentException("Too Busy")
        }

        taskResources()
    }
}
