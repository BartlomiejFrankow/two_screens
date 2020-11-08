package com.example.twoscreens.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

interface UpdateTask {
    fun invoke(id: String, title: String, description: String, iconUrl: String): Task<Void>
}

class UpdateTaskImpl(private val fireStore: FirebaseFirestore) : UpdateTask {
    override fun invoke(id: String, title: String, description: String, iconUrl: String): Task<Void> {

        val updateTask: MutableMap<String, Any> = HashMap()
        updateTask[TITLE] = title
        updateTask[DESCRIPTION] = description
        updateTask[CREATION_DATE] = Timestamp.now()
        if (iconUrl.isNotEmpty()) updateTask[ICON] = iconUrl

        return fireStore.collection(TASKS_COLLECTION).document(id).update(updateTask)
    }
}
