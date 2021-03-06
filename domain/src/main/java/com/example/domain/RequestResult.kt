package com.example.domain

sealed class RequestResult<out BODY> {
    object Error : RequestResult<Nothing>()
    data class Success<T>(val body: T) : RequestResult<T>()
}