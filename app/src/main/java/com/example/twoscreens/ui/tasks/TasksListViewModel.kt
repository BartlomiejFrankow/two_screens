package com.example.twoscreens.ui.tasks

import com.example.twoscreens.Event
import com.example.twoscreens.R
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.ObserveTasksCollection
import com.example.twoscreens.firebase.PAGINATION_LIMIT
import com.example.twoscreens.firebase.TasksResponse
import com.example.twoscreens.firebase.RequestResult.Success
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TasksListViewModel(
    private val observeTasksCollection: ObserveTasksCollection,
    private val deleteTask: DeleteTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    private val stateStore = StateStore(TasksListViewState())
    var wasLastItemReached = false

    val onSuccessRemove = Event<Int>()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        scope.launch {
            observeTasksCollection.observeTasks(response = { results ->
                clearTasksList()
                if (results is Success) {
                    wasLastItemReached = false
                    stateStore.setState {
                        copy(
                            tasks = results.body.documents.toMutableList(),
                            lastKnownDocument = results.body.lastDocument
                        )
                    }
                }
            })
        }
    }

    override fun observeState() = stateStore.observe()

    fun getNextTasks() {
        scope.launch {
            observeTasksCollection.getNextTasks(stateStore.currentState.lastKnownDocument!!, response = { results ->
                if (results is Success) mergeTasksAndUpdateState(results.body)
            })
        }
    }

    private fun mergeTasksAndUpdateState(response: TasksResponse) {
        val snapShotSize = response.documents.size
        wasLastItemReached = snapShotSize < PAGINATION_LIMIT
        val mergedTasks = stateStore.currentState.tasks!!.apply { this.addAll(response.documents) }

        stateStore.setState { copy(tasks = mergedTasks, lastKnownDocument = response.lastDocument) }
    }

    fun removeTask(id: String) {
        val hasLastTaskBeforeRemove = stateStore.currentState.hasOnlyOneListElement
        scope.launch {
            deleteTask.invoke(id, response = { results ->
                if (results is Success) {
                    if (hasLastTaskBeforeRemove) clearTasksList()
                    onSuccessRemove.postEvent(R.string.removed)
                }
            })
        }
    }

    private fun clearTasksList() = stateStore.setState { copy(tasks = mutableListOf()) }

}
