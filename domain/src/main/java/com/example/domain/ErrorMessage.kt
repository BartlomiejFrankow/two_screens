package com.example.domain

interface ErrorMessage {
    fun show(message: String?)
    fun show(resource: Int)
}
