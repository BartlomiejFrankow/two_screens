package com.example.network.impl

import com.example.domain.*
import com.example.domain.dto.Description
import com.example.domain.dto.ImageUrl
import com.example.domain.dto.Title
import com.example.network.ErrorExecutor
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class CreateTaskImpl(private val fireStore: FirebaseFirestore, private val errorExecutor: ErrorExecutor) : CreateTask {

    override suspend fun invoke(title: Title, description: Description, iconUrl: ImageUrl?, response: (RequestResult<Unit>) -> Unit) {
        val task: MutableMap<String, Any> = HashMap()
        task[TITLE] = title.value
        task[DESCRIPTION] = description.value
        task[CREATION_DATE] = Timestamp.now()
        iconUrl?.value?.let { task[ICON] = it }

        fireStore
            .collection(TASKS_COLLECTION)
            .add(task)
            .addOnCompleteListener { body ->
                when {
                    body.isSuccessful -> response(RequestResult.Success(Unit))
                    else -> errorExecutor.execute(body.exception?.message)
                }
            }
    }
}