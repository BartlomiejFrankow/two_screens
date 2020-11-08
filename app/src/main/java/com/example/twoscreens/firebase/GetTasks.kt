package com.example.twoscreens.firebase

import com.example.twoscreens.firebase.responses.GetTasksResponse
import com.example.twoscreens.firebase.responses.GetTasksResponse.Error
import com.example.twoscreens.firebase.responses.GetTasksResponse.Success
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING

const val PAGINATION_LIMIT = 30L

interface GetTasks {
    suspend fun observeTasks(response: (GetTasksResponse) -> Unit)
    suspend fun getNextTasks(document: DocumentSnapshot, response: (GetTasksResponse) -> Unit)
}

class GetTasksImpl(fireStore: FirebaseFirestore, private val firebaseError: FirebaseError) : GetTasks {

    private val collection = fireStore.collection(TASKS_COLLECTION)

    override suspend fun observeTasks(response: (GetTasksResponse) -> Unit) {
        collection
            .orderBy(CREATION_DATE, DESCENDING)
            .limit(PAGINATION_LIMIT)
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> response(Error(firebaseError.getMessage(error.code)))
                    snapshot != null -> response(Success(snapshot.documents))
                }
            }
    }

    override suspend fun getNextTasks(document: DocumentSnapshot, response: (GetTasksResponse) -> Unit) {
        collection
            .orderBy(CREATION_DATE, DESCENDING)
            .startAfter(document)
            .limit(PAGINATION_LIMIT)
            .get()
            .addOnSuccessListener { snapshot -> response(Success(snapshot.documents)) }
            .addOnFailureListener { exception -> exception.message?.let { response(Error(it)) } }
    }

}
