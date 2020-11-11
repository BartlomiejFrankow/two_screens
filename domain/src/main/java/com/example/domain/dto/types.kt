package com.example.domain.dto

import java.io.Serializable

data class TaskId(val value: String) : Serializable

data class Title(val value: String) : Serializable

data class Description(val value: String) : Serializable

data class ImageUrl(val value: String) : Serializable
