package com.example.controller

import com.example.domain.Priority
import com.example.domain.Task
import com.example.infrastructure.TaskRepository
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

@Resource("/tasks")
class Tasks()

@Resource("/tasks/priority/{priority}")
class TasksByPriority()

@Resource("/tasks/{id}")
class TaskById()

fun Route.taskResources() {
    get<Tasks> {
        val tasks = TaskRepository.getAll()
        call.respond(tasks)
    }

    get<TasksByPriority> {
        val priorityAsString = call.pathParameters["priority"]
        if (priorityAsString == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        try {
            val priority = Priority.valueOf(priorityAsString.uppercase())
            val tasks = TaskRepository.getTasksBy(priority)

            call.respond(tasks)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    post<Tasks> {
        try {
            val task = call.receive<Task>()
            TaskRepository.register(task)
            call.respond(HttpStatusCode.Created)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (e: JsonConvertException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    delete<TaskById> {
        val idAsString = call.pathParameters["id"]
        if (idAsString == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        try {
            val id = UUID.fromString(idAsString)
            TaskRepository.remove(id)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        call.respond(HttpStatusCode.OK)
    }
}
