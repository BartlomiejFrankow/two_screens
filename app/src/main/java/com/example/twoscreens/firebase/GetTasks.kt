package com.example.twoscreens.firebase

import com.example.twoscreens.firebase.results.GetTasksResponse
import com.example.twoscreens.firebase.results.GetTasksResponse.Error
import com.example.twoscreens.firebase.results.GetTasksResponse.Success
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING

const val PAGINATION_LIMIT = 20L

interface GetTasks {
    fun observeTasks(response: (GetTasksResponse) -> Unit)
    fun getNextTasks(document: DocumentSnapshot, response: (GetTasksResponse) -> Unit)
}

class GetTasksImpl(fireStore: FirebaseFirestore) : GetTasks {

    private val collection = fireStore.collection(TASKS_COLLECTION)

    override fun observeTasks(response: (GetTasksResponse) -> Unit) {
        collection
            .orderBy(CREATION_DATE, DESCENDING)
            .limit(PAGINATION_LIMIT)
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> response(Error(FirebaseError().getMessage(error.code)))
                    snapshot != null -> response(Success(snapshot.documents))
                }
            }
    }

    override fun getNextTasks(document: DocumentSnapshot, response: (GetTasksResponse) -> Unit) {
        collection
            .orderBy(CREATION_DATE, DESCENDING)
            .startAfter(document)
            .limit(PAGINATION_LIMIT)
            .get()
            .addOnSuccessListener { snapshot -> response(Success(snapshot.documents)) }
            .addOnFailureListener { exception -> exception.message?.let { response(Error(it)) } }
    }

}
