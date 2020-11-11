package com.example.domain

import com.example.domain.dto.Description
import com.example.domain.dto.ImageUrl
import com.example.domain.dto.Title

const val TASKS_COLLECTION = "tasks"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val ICON = "icon"
const val CREATION_DATE = "creationDate"

interface CreateTask {
    suspend operator fun invoke(title: Title, description: Description, iconUrl: ImageUrl?, response: (RequestResult<Unit>) -> Unit)
}
