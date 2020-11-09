package com.example.twoscreens.firebase

import com.example.twoscreens.firebase.RequestResult.Success
import com.example.twoscreens.toMilli
import com.example.twoscreens.ui.tasks.TaskItemDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import org.threeten.bp.Instant

const val PAGINATION_LIMIT = 30L

interface ObserveTasksCollection {
    suspend fun observeTasks(response: (RequestResult<TasksResponse>) -> Unit)
    suspend fun getNextTasks(document: DocumentSnapshot, response: (RequestResult<TasksResponse>) -> Unit)
}

class ObserveTasksCollectionImpl(fireStore: FirebaseFirestore, private val errorExecutor: FirebaseErrorExecutor) : ObserveTasksCollection {

    private val collection = fireStore.collection(TASKS_COLLECTION)

    override suspend fun observeTasks(response: (RequestResult<TasksResponse>) -> Unit) {
        collection
            .orderBy(CREATION_DATE, DESCENDING)
            .limit(PAGINATION_LIMIT)
            .addSnapshotListener { snapshot, error ->
                when {
                    snapshot != null -> response(Success(TasksResponse(snapshot.documents.mapToDto(), snapshot.documents.lastOrNull())))
                    error != null -> errorExecutor.execute(error.code)
                }
            }
    }

    override suspend fun getNextTasks(document: DocumentSnapshot, response: (RequestResult<TasksResponse>) -> Unit) {
        collection
            .orderBy(CREATION_DATE, DESCENDING)
            .startAfter(document)
            .limit(PAGINATION_LIMIT)
            .get()
            .addOnCompleteListener { body->
                when{
                    body.isSuccessful -> response(Success(TasksResponse(body.result!!.documents.mapToDto(), body.result!!.documents.lastOrNull())))
                    else -> body.exception?.message?.let { errorExecutor.execute(it) }
                }
            }
    }

}

private fun List<DocumentSnapshot>.mapToDto(): List<TaskItemDto> {
    return this.map { document ->
        TaskItemDto(
            id = document.id,
            title = (document.data!![TITLE] as String),
            description = (document.data!![DESCRIPTION] as String),
            iconUrl = if (document.data!![ICON] != null) document.data!![ICON] as String else null,
            creationDate = Instant.ofEpochMilli((document.data!![CREATION_DATE] as Timestamp).toMilli())
        )
    }
}

data class TasksResponse(val documents: List<TaskItemDto>, val lastDocument: DocumentSnapshot?)
