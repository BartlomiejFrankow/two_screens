package com.example.domain.dto

import org.threeten.bp.Instant
import java.io.Serializable

data class TaskItemDto(val id: TaskId, val title: Title, val description: Description, val iconUrl: ImageUrl?, val creationDate: Instant) : Serializable
