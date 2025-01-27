package com.example

import com.example.domain.Priority
import com.example.domain.Task
import com.example.infrastructure.TaskRepository
import com.example.util.jsonSerializer
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
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

        route("/tasks") {
            get {
                val tasks = TaskRepository.getAllTasks()
                call.respond(tasks)
            }

            get("/priority/{priority}") {
                val priorityAsString = call.parameters["priority"]
                if (priorityAsString == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                try {
                    val priority = Priority.valueOf(priorityAsString.uppercase())
                    val tasks = TaskRepository.getTasksBy(priority)

                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(tasks)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                try {
                    val task = call.receive<Task>()
                    TaskRepository.addTask(task)
                    call.respond(HttpStatusCode.Created)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (e: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{id}") {
                val idAsString = call.parameters["id"]
                if (idAsString == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                try {
                    val id = UUID.fromString(idAsString)

                    if (TaskRepository.remove(id)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
            }
        }
    }
}
