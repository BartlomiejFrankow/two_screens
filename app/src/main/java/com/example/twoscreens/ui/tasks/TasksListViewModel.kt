package com.example.twoscreens.ui.tasks

import com.example.twoscreens.Event
import com.example.twoscreens.R
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.GetTasks
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope

class TasksListViewModel(
    getTasks: GetTasks,
    private val deleteTask: DeleteTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    private val stateStore = StateStore(TasksListViewState())

    val doOnError = Event<String>()
    val onSuccessRemove = Event<Int>()

    init {
        getTasks.getFirstTasks()
            .addOnSuccessListener { snapshot -> stateStore.setState { copy(snapshot = snapshot.documents) } }
            .addOnFailureListener { exception -> exception.message?.let { doOnError.postEvent(it) } }
    }

    override fun observeState() = stateStore.observe()

    fun removeTask(id: String) {
        deleteTask.invoke(id)
            .addOnSuccessListener { onSuccessRemove.postEvent(R.string.removed) } // fire store watcher will update list automatically
            .addOnFailureListener { exception -> exception.message?.let { doOnError.postEvent(it) } }
    }

}
