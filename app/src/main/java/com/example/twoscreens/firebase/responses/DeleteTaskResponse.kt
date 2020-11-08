package com.example.twoscreens.firebase.responses

sealed class DeleteTaskResponse {
    data class Success(val message: Int) : DeleteTaskResponse()
    data class Error(val message: String) : DeleteTaskResponse()
}
