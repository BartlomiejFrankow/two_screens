package com.example.twoscreens.ui.tasks

import com.example.twoscreens.Event
import com.example.twoscreens.R
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.PAGINATION_LIMIT_STEP
import com.example.twoscreens.firebase.RequestResult.Success
import com.example.twoscreens.firebase.TasksCollection
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TasksListViewModel(
    private val tasksCollection: TasksCollection,
    private val deleteTask: DeleteTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    val stateStore = StateStore(TasksListViewState())

    private var actualPaginationSize = 0L

    val onSuccessRemove = Event<Int>()

    init {
        runObserver()
    }

    override fun observeState() = stateStore.observe()

    private fun runObserver() {
        actualPaginationSize += PAGINATION_LIMIT_STEP

        scope.launch {
            tasksCollection.observe(
                paginationLimit = actualPaginationSize,
                response = { results ->
                    clearTasksList()
                    if (results is Success) { stateStore.setState { copy(tasks = results.body) } }
                })
        }
    }

    fun checkIfNeedToIncreasePagination() {
        if (stateStore.currentState.items.size >= actualPaginationSize)
            runObserver()
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

    private fun clearTasksList() = stateStore.setState { copy(tasks = listOf()) }

}
