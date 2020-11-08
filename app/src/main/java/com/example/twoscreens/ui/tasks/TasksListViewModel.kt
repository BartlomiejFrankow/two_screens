package com.example.twoscreens.ui.tasks

import com.example.twoscreens.Event
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.GetTasks
import com.example.twoscreens.firebase.PAGINATION_LIMIT
import com.example.twoscreens.firebase.responses.DeleteTaskResponse
import com.example.twoscreens.firebase.responses.GetTasksResponse.Error
import com.example.twoscreens.firebase.responses.GetTasksResponse.Success
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TasksListViewModel(
    private val getTasks: GetTasks,
    private val deleteTask: DeleteTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    private val stateStore = StateStore(TasksListViewState())
    var wasLastItemReached = false

    val onError = Event<String>()
    val onSuccessRemove = Event<Int>()

    init {
        scope.launch {
            getTasks.observeTasks(response = { results ->
                when (results) {
                    is Success -> {
                        wasLastItemReached = false
                        if (results.documents.isNotEmpty())
                            stateStore.setState {
                                copy(
                                    tasks = results.documents.toMutableList(),
                                    lastKnownDocument = results.documents[results.documents.size - 1]
                                )
                            }
                    }
                    is Error -> onError.postEvent(results.message)
                }
            })
        }
    }

    override fun observeState() = stateStore.observe()

    fun getNextTasks() {
        scope.launch {
            getTasks.getNextTasks(stateStore.currentState.lastKnownDocument!!, response = { results ->
                when (results) {
                    is Success -> mergeTasksAndUpdateState(results)
                    is Error -> onError.postEvent(results.message)
                }
            })
        }
    }

    private fun mergeTasksAndUpdateState(results: Success) {
        val snapShotSize = results.documents.size
        wasLastItemReached = snapShotSize < PAGINATION_LIMIT
        val mergedTasks = stateStore.currentState.tasks!!.apply { this.addAll(results.documents) }

        stateStore.setState { copy(tasks = mergedTasks, lastKnownDocument = mergedTasks.last()) }
    }

    fun removeTask(id: String) {
        scope.launch {
            deleteTask.invoke(id, response = { results ->
                when (results) {
                    is DeleteTaskResponse.Success -> onSuccessRemove.postEvent(results.message)
                    is DeleteTaskResponse.Error -> onError.postEvent(results.message)
                }
            })
        }
    }

}
