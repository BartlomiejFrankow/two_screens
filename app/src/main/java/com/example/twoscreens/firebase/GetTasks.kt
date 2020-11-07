package com.example.twoscreens.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.google.firebase.firestore.QuerySnapshot

private const val PAGINATION_LIMIT = 20L

interface GetTasks {
    fun getFirstTasks(): Task<QuerySnapshot>
    fun getNextTasks(id: String): Task<QuerySnapshot>
}

class GetTasksImpl(fireStore: FirebaseFirestore) : GetTasks {

    private val collection = fireStore.collection(TASKS_COLLECTION)

    override fun getFirstTasks() = collection
        .orderBy(CREATION_DATE, DESCENDING)
        .limit(PAGINATION_LIMIT)
        .get()

    override fun getNextTasks(id: String) = collection
        .orderBy(CREATION_DATE, DESCENDING)
        .startAfter(collection.document(id))
        .get()

}
