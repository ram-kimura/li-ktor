package com.example

import com.example.domain.Priority
import com.example.domain.Task
import com.example.infrastructure.TaskRepository
import com.example.util.jsonSerializer
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import java.util.*

@Resource("/tasks")
class Tasks()

@Resource("/tasks/priority/{priority}")
class TasksByPriority()

@Resource("/tasks/{id}")
class TaskById()

fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    install(ContentNegotiation) {
        json(jsonSerializer)
    }

    install(Resources)
    install(CallLogging)

    val env = environment.config.propertyOrNull("ktor.environment")?.getString() ?: "development"
    val logger = LoggerFactory.getLogger(this::class.java)
    logger.info("Environment: $env")

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

        get<Tasks> {
            val tasks = TaskRepository.getAllTasks()
            call.respond(tasks)
        }

        get<TasksByPriority> {
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

        post<Tasks> {
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

        delete<TaskById> {
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
