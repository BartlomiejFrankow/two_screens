package com.example.twoscreens.ui.tasks

import com.google.firebase.firestore.DocumentSnapshot

data class TasksListViewState(
    val tasks: MutableList<TaskItemDto>? = null,
    val lastKnownDocument: DocumentSnapshot? = null
)

val TasksListViewState.items
    get() = when {
        tasks.isNullOrEmpty() -> emptyList()
        else -> tasks.toList()
    }

val TasksListViewState.showLoading
    get() = tasks == null

val TasksListViewState.showEmptyInfo
    get() = tasks != null && items.isEmpty()

val TasksListViewState.hasOnlyOneListElement
    get() = tasks?.size == 1
