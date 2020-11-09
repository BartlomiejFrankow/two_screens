package com.example.twoscreens.firebase

import com.example.twoscreens.firebase.RequestResult.Success
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

const val TASKS_COLLECTION = "tasks"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val ICON = "icon"
const val CREATION_DATE = "creationDate"

interface CreateTask {
    suspend fun invoke(title: String, description: String, iconUrl: String, response: (RequestResult<Unit>) -> Unit)
}

class CreateTaskImpl(private val fireStore: FirebaseFirestore, private val errorExecutor: FirebaseErrorExecutor) : CreateTask {

    override suspend fun invoke(title: String, description: String, iconUrl: String, response: (RequestResult<Unit>) -> Unit) {
        val task: MutableMap<String, Any> = HashMap()
        task[TITLE] = title
        task[DESCRIPTION] = description
        task[CREATION_DATE] = Timestamp.now()
        if (iconUrl.isNotEmpty()) task[ICON] = iconUrl

        fireStore
            .collection(TASKS_COLLECTION)
            .add(task)
            .addOnCompleteListener { body ->
                when {
                    body.isSuccessful -> response(Success(Unit))
                    else -> body.exception?.message?.let { errorExecutor.execute(it) }
                }
            }
    }
}
