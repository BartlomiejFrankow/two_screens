package com.example.twoscreens.firebase

import com.example.twoscreens.R
import com.example.twoscreens.firebase.responses.CreateOrUpdateTaskResponse
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

interface UpdateTask {
    suspend fun invoke(id: String, title: String, description: String, iconUrl: String, response: (CreateOrUpdateTaskResponse) -> Unit)
}

class UpdateTaskImpl(private val fireStore: FirebaseFirestore) : UpdateTask {
    override suspend fun invoke(id: String, title: String, description: String, iconUrl: String, response: (CreateOrUpdateTaskResponse) -> Unit) {

        val updateTask: MutableMap<String, Any> = HashMap()
        updateTask[TITLE] = title
        updateTask[DESCRIPTION] = description
        if (iconUrl.isNotEmpty()) updateTask[ICON] = iconUrl

        fireStore
            .collection(TASKS_COLLECTION)
            .document(id)
            .update(updateTask)
            .addOnSuccessListener { response(CreateOrUpdateTaskResponse.Success(R.string.task_created)) }
            .addOnFailureListener { error -> error.message?.let { response(CreateOrUpdateTaskResponse.Error(it)) } }
    }
}
