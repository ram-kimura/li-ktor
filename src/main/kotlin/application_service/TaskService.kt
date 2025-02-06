package com.example.application_service

import com.example.domain.Task
import com.example.infrastructure.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TaskService {
    suspend fun getAll(): List<Task> {
        return withContext(Dispatchers.IO) { TaskRepository.getAll() }
    }
}
