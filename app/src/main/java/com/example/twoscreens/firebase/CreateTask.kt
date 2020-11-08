package com.example.twoscreens.firebase

import com.example.twoscreens.R
import com.example.twoscreens.firebase.responses.CreateOrUpdateTaskResponse
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

const val TASKS_COLLECTION = "tasks"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val ICON = "icon"
const val CREATION_DATE = "creationDate"

interface CreateTask {
    suspend fun invoke(title: String, description: String, iconUrl: String, response: (CreateOrUpdateTaskResponse) -> Unit)
}

class CreateTaskImpl(private val fireStore: FirebaseFirestore) : CreateTask {
    override suspend fun invoke(title: String, description: String, iconUrl: String, response: (CreateOrUpdateTaskResponse) -> Unit) {

        val task: MutableMap<String, Any> = HashMap()
        task[TITLE] = title
        task[DESCRIPTION] = description
        task[CREATION_DATE] = Timestamp.now()
        if (iconUrl.isNotEmpty()) task[ICON] = iconUrl

        fireStore
            .collection(TASKS_COLLECTION)
            .add(task)
            .addOnSuccessListener { response(CreateOrUpdateTaskResponse.Success(R.string.task_created)) }
            .addOnFailureListener { error -> error.message?.let { response(CreateOrUpdateTaskResponse.Error(it)) } }
    }
}
