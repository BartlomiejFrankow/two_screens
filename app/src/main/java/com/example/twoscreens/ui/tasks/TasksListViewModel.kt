package com.example.twoscreens.ui.tasks

import com.example.twoscreens.Event
import com.example.twoscreens.R
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.GetTasks
import com.example.twoscreens.firebase.PAGINATION_LIMIT
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope

class TasksListViewModel(
    private val getTasks: GetTasks,
    private val deleteTask: DeleteTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    private val stateStore = StateStore(TasksListViewState())
    var wasLastItemReached = false

    val doOnError = Event<String>()
    val onSuccessRemove = Event<Int>()
    val onNextPageLoaded = Event<Int>()

    init {
        getTasks.getFirstTasks()
            .addOnSuccessListener { results ->
                stateStore.setState { copy(documents = results.documents, lastKnownDocument = results.documents[results.size() - 1]) }
            }
            .addOnFailureListener { exception ->
                exception.message?.let { doOnError.postEvent(it) }
            }
    }

    override fun observeState() = stateStore.observe()

    fun getNextTasks() {
        getTasks.getNextTasks(stateStore.currentState.lastKnownDocument!!)
            .addOnSuccessListener { results ->
                onNextPageLoaded.postEvent(R.string.next_page_loaded)
                wasLastItemReached = results.size() < PAGINATION_LIMIT
                val mergedTasks = stateStore.currentState.documents!!
                mergedTasks.addAll(results.documents)
                stateStore.setState { copy(documents = mergedTasks, lastKnownDocument = results.documents[results.documents.size - 1]) }
            }
            .addOnFailureListener { exception ->
                exception.message?.let { doOnError.postEvent(it) }
            }
    }

    fun removeTask(id: String) {
        deleteTask.invoke(id)
            .addOnSuccessListener { onSuccessRemove.postEvent(R.string.removed) } // fire store watcher will update list automatically
            .addOnFailureListener { exception -> exception.message?.let { doOnError.postEvent(it) } }
    }

}
