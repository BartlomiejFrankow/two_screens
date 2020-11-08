package com.example.twoscreens.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

interface DeleteTask {
    fun invoke(id: String): Task<Void>
}

class DeleteTaskImpl(private val fireStore: FirebaseFirestore) : DeleteTask {
    override fun invoke(id: String): Task<Void> {
        return fireStore.collection(TASKS_COLLECTION).document(id).delete()
    }
}
