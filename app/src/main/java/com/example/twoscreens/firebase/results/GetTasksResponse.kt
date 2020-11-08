package com.example.twoscreens.firebase.results

import com.google.firebase.firestore.DocumentSnapshot

sealed class GetTasksResponse {
    data class Success(val documents: List<DocumentSnapshot>) : GetTasksResponse()
    data class Error(val message: String) : GetTasksResponse()
}
