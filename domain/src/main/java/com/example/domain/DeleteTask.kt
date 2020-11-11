package com.example.domain

import com.example.domain.dto.TaskId

interface DeleteTask {
    suspend operator fun invoke(id: TaskId, response: (RequestResult<Unit>) -> Unit)
}
