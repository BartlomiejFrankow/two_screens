package com.example.twoscreens.firebase

import com.example.twoscreens.R
import com.example.twoscreens.firebase.responses.DeleteTaskResponse
import com.google.firebase.firestore.FirebaseFirestore

interface DeleteTask {
    suspend fun invoke(id: String, response: (DeleteTaskResponse) -> Unit)
}

class DeleteTaskImpl(private val fireStore: FirebaseFirestore) : DeleteTask {
    override suspend fun invoke(id: String, response: (DeleteTaskResponse) -> Unit) {
        fireStore
            .collection(TASKS_COLLECTION)
            .document(id)
            .delete()
            .addOnSuccessListener { response(DeleteTaskResponse.Success(R.string.removed)) }
            .addOnFailureListener { error -> error.message?.let { response(DeleteTaskResponse.Error(it)) } }
    }
}
