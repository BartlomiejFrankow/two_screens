package com.example.twoscreens.ui.todo

import com.example.twoscreens.Event
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.GetTasks
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope

class TasksListViewModel(
    getTasks: GetTasks,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    private val stateStore = StateStore(TasksListViewState())

    val doOnError = Event<String>()

    init {
        getTasks.invoke()
            .addOnSuccessListener { snapshot -> stateStore.setState { copy(snapshot = snapshot.documents) } }
            .addOnFailureListener { exception -> exception.message?.let { doOnError.postEvent(it) } }
    }

    override fun observeState() = stateStore.observe()

}
