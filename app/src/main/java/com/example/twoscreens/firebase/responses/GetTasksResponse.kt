package com.example.twoscreens.firebase.responses

import com.google.firebase.firestore.DocumentSnapshot

sealed class GetTasksResponse {
    data class Success(val documents: List<DocumentSnapshot>) : GetTasksResponse()
    data class Error(val message: String) : GetTasksResponse()
}
