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

fun Route.tasksResources() {
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
