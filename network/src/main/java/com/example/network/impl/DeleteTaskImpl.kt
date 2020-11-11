package com.example.network.impl

import com.example.domain.DeleteTask
import com.example.domain.RequestResult
import com.example.domain.TASKS_COLLECTION
import com.example.domain.dto.TaskId
import com.example.network.ErrorExecutor
import com.google.firebase.firestore.FirebaseFirestore

class DeleteTaskImpl(private val fireStore: FirebaseFirestore, private val errorExecutor: ErrorExecutor) : DeleteTask {
    override suspend fun invoke(id: TaskId, response: (RequestResult<Unit>) -> Unit) {
        fireStore
            .collection(TASKS_COLLECTION)
            .document(id.value)
            .delete()
            .addOnCompleteListener { body ->
                when {
                    body.isSuccessful -> response(RequestResult.Success(Unit))
                    else -> errorExecutor.execute(body.exception?.message)
                }
            }
    }
}
