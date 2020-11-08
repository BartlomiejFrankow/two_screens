package com.example.twoscreens.ui.tasks

import com.example.twoscreens.Event
import com.example.twoscreens.R
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.GetTasks
import com.example.twoscreens.firebase.PAGINATION_LIMIT
import com.example.twoscreens.firebase.results.DeleteTaskResponse
import com.example.twoscreens.firebase.results.GetTasksResponse.Error
import com.example.twoscreens.firebase.results.GetTasksResponse.Success
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

    val onError = Event<String>()
    val onSuccessRemove = Event<Int>()
    val onNextPageLoaded = Event<Int>()

    init {
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

    override fun observeState() = stateStore.observe()

    fun getNextTasks() {
        getTasks.getNextTasks(stateStore.currentState.lastKnownDocument!!, response = { results ->
            when (results) {
                is Success -> mergeTasksAndUpdateState(results)
                is Error -> onError.postEvent(results.message)
            }
        })
    }

    private fun mergeTasksAndUpdateState(results: Success) {
        val snapShotSize = results.documents.size
        wasLastItemReached = snapShotSize < PAGINATION_LIMIT

        if (snapShotSize > 0) {
            stateStore.setState {
                copy(
                    tasks = stateStore.currentState.tasks!!.apply { this.addAll(results.documents) },
                    lastKnownDocument = results.documents[snapShotSize - 1]
                )
            }
            onNextPageLoaded.postEvent(R.string.next_page_loaded)
        }
    }

    fun removeTask(id: String) {
        deleteTask.invoke(id, response = { results ->
            when (results) {
                is DeleteTaskResponse.Success -> onSuccessRemove.postEvent(results.message)
                is DeleteTaskResponse.Error -> onError.postEvent(results.message)
            }
        })
    }

}
