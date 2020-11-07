package com.example.twoscreens.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

interface GetTasks {
    fun invoke(): Task<QuerySnapshot>
}

class GetTasksImpl(private val fireStore: FirebaseFirestore): GetTasks {
    override fun invoke() = fireStore.collection(TASKS_COLLECTION).get()
}