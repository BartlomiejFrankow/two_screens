package com.example.network.impl

import com.example.domain.*
import com.example.domain.dto.*
import com.example.network.ErrorExecutor
import com.example.network.toMilli
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import org.threeten.bp.Instant

class ObserveTasksImpl(fireStore: FirebaseFirestore, private val errorExecutor: ErrorExecutor) : ObserveTasks {

    private val collection = fireStore.collection(TASKS_COLLECTION)

    private var fireStoreObserver: ListenerRegistration? = null

    override suspend fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit) {
        fireStoreObserver?.remove()

        fireStoreObserver = collection
            .orderBy(CREATION_DATE, Query.Direction.DESCENDING)
            .limit(paginationLimit)
            .addSnapshotListener { snapshot, error ->
                when {
                    snapshot != null -> response(RequestResult.Success(snapshot.documents.mapToDto()))
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
            id = TaskId(document.id),
            title = Title(document.data!![TITLE] as String),
            description = Description(document.data!![DESCRIPTION] as String),
            iconUrl = if (document.data!![ICON] != null) ImageUrl(document.data!![ICON] as String) else null,
            creationDate = Instant.ofEpochMilli((document.data!![CREATION_DATE] as Timestamp).toMilli())
        )
    }
}
