package com.example.twoscreens.firebase.responses

sealed class CreateOrUpdateTaskResponse {
    data class Success(val message: Int) : CreateOrUpdateTaskResponse()
    data class Error(val message: String) : CreateOrUpdateTaskResponse()
}