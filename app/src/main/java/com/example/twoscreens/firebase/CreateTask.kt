package com.example.twoscreens.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

const val TASKS_COLLECTION = "tasks"
const val TITLE = "title"
const val DESCRIPTION = "description"
const val ICON = "icon"
const val CREATION_DATE = "creationDate"

interface CreateTask {
    fun invoke(title: String, description: String, iconUrl: String): Task<DocumentReference>
}

class CreateTaskImpl(private val fireStore: FirebaseFirestore) : CreateTask {
    override fun invoke(title: String, description: String, iconUrl: String): Task<DocumentReference> {

        val task: MutableMap<String, Any> = HashMap()
        task[TITLE] = title
        task[DESCRIPTION] = description
        task[CREATION_DATE] = Timestamp.now()
        if (iconUrl.isNotEmpty()) task[ICON] = iconUrl

        return fireStore.collection(TASKS_COLLECTION).add(task)
    }
}
