package com.example.twoscreens.firebase

sealed class RequestResult<out BODY> {
    data class Success<T>(val body: T) : RequestResult<T>()
}
