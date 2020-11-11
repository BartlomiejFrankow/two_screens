package com.example.domain

import com.example.domain.dto.TaskItemDto

const val PAGINATION_LIMIT_STEP = 30L

interface ObserveTasks {
    suspend operator fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit)
}
