package com.example.domain

import com.example.domain.dto.Description
import com.example.domain.dto.ImageUrl
import com.example.domain.dto.TaskId
import com.example.domain.dto.Title

interface UpdateTask {
    suspend operator fun invoke(id: TaskId, title: Title, description: Description, iconUrl: ImageUrl?, response: (RequestResult<Unit>) -> Unit)
}
