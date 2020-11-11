package com.example.twoscreens.firebase

import com.example.twoscreens.firebase.RequestResult.Success
import com.example.twoscreens.toMilli
import com.example.twoscreens.ui.tasks.TaskItemDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query.Direction.DESCENDING
import org.threeten.bp.Instant

const val PAGINATION_LIMIT_STEP = 30L

interface ObserveTasks {
    suspend operator fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit)
}

class ObserveTasksImpl(fireStore: FirebaseFirestore, private val errorExecutor: FirebaseErrorExecutor) : ObserveTasks {

    private val collection = fireStore.collection(TASKS_COLLECTION)

    private var fireStoreObserver: ListenerRegistration? = null

    override suspend fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit) {
        fireStoreObserver?.remove()

        fireStoreObserver = collection
            .orderBy(CREATION_DATE, DESCENDING)
            .limit(paginationLimit)
            .addSnapshotListener { snapshot, error ->
                when {
                    snapshot != null -> response(Success(snapshot.documents.mapToDto()))
                    error != null -> {
                        response(RequestResult.Error)
                        errorExecutor.execute(error.code)
                    }
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
