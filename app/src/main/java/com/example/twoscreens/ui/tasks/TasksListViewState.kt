package com.example.twoscreens.ui.tasks

import com.example.twoscreens.firebase.CREATION_DATE
import com.example.twoscreens.firebase.DESCRIPTION
import com.example.twoscreens.firebase.ICON
import com.example.twoscreens.firebase.TITLE
import com.example.twoscreens.toMilli
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import org.threeten.bp.Instant

data class TasksListViewState(
    val snapshot: List<DocumentSnapshot>? = null
)

val TasksListViewState.items
    get() = when {
        snapshot.isNullOrEmpty() -> emptyList()
        else -> mapToItem(snapshot)
    }

val TasksListViewState.showLoading
    get() = snapshot == null

val TasksListViewState.showEmptyInfo
    get() = snapshot != null && snapshot.isEmpty()

private fun mapToItem(snapshot: List<DocumentSnapshot>): List<TaskItemDto> {
    return snapshot.map { document ->
        TaskItemDto(
            id = document.id,
            title = (document.data!![TITLE] as String),
            description = (document.data!![DESCRIPTION] as String),
            iconUrl = document.data!![ICON]?.let { it as String },
            creationDate = Instant.ofEpochMilli((document.data!![CREATION_DATE] as Timestamp).toMilli())
        )
    }
}
