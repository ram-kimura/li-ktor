package com.example.application_service

import com.example.domain.Task
import com.example.infrastructure.TaskRepository

object TaskService {
    fun getAll(): List<Task> = TaskRepository.getAll()
}
