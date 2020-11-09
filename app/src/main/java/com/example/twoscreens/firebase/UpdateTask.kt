package com.example.twoscreens.firebase

import com.example.twoscreens.firebase.RequestResult.Success
import com.google.firebase.firestore.FirebaseFirestore

interface UpdateTask {
    suspend fun invoke(id: String, title: String, description: String, iconUrl: String, response: (RequestResult<Unit>) -> Unit)
}

class UpdateTaskImpl(private val fireStore: FirebaseFirestore, private val errorExecutor: FirebaseErrorExecutor) : UpdateTask {
    override suspend fun invoke(id: String, title: String, description: String, iconUrl: String, response: (RequestResult<Unit>) -> Unit) {

        val updateTask: MutableMap<String, Any?> = HashMap()
        updateTask[TITLE] = title
        updateTask[DESCRIPTION] = description
        updateTask[ICON] = if (iconUrl.isNotEmpty()) iconUrl else null

        fireStore
            .collection(TASKS_COLLECTION)
            .document(id)
            .update(updateTask)
            .addOnCompleteListener { body ->
                when {
                    body.isSuccessful -> response(Success(Unit))
                    else -> body.exception?.message?.let { errorExecutor.execute(it) }
                }
            }
    }
}
