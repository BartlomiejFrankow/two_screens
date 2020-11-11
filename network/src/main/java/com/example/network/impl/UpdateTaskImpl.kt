package com.example.network.impl

import com.example.domain.*
import com.example.domain.dto.Description
import com.example.domain.dto.ImageUrl
import com.example.domain.dto.TaskId
import com.example.domain.dto.Title
import com.example.network.ErrorExecutor
import com.google.firebase.firestore.FirebaseFirestore

class UpdateTaskImpl(private val fireStore: FirebaseFirestore, private val errorExecutor: ErrorExecutor) : UpdateTask {
    override suspend fun invoke(id: TaskId, title: Title, description: Description, iconUrl: ImageUrl?, response: (RequestResult<Unit>) -> Unit) {

        val updateTask: MutableMap<String, Any?> = HashMap()
        updateTask[TITLE] = title.value
        updateTask[DESCRIPTION] = description.value
        updateTask[ICON] = if (iconUrl?.value != null) iconUrl.value else null

        fireStore
            .collection(TASKS_COLLECTION)
            .document(id.value)
            .update(updateTask)
            .addOnCompleteListener { body ->
                when {
                    body.isSuccessful -> response(RequestResult.Success(Unit))
                    else -> errorExecutor.execute(body.exception?.message)
                }
            }
    }
}
