package com.example.twoscreens.firebase

import com.google.firebase.firestore.FirebaseFirestore

interface DeleteTask {
    suspend operator fun invoke(id: String, response: (RequestResult<Unit>) -> Unit)
}

class DeleteTaskImpl(private val fireStore: FirebaseFirestore, private val errorExecutor: FirebaseErrorExecutor) : DeleteTask {
    override suspend fun invoke(id: String, response: (RequestResult<Unit>) -> Unit) {
        fireStore
            .collection(TASKS_COLLECTION)
            .document(id)
            .delete()
            .addOnCompleteListener { body->
                when {
                    body.isSuccessful -> response(RequestResult.Success(Unit))
                    else -> body.exception?.message?.let { errorExecutor.execute(it) }
                }
            }
    }
}
