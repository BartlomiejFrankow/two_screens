package com.example.twoscreens.ui.tasks

import com.example.domain.DeleteTask
import com.example.domain.ObserveTasks
import com.example.domain.PAGINATION_LIMIT_STEP
import com.example.domain.RequestResult.Error
import com.example.domain.RequestResult.Success
import com.example.domain.dto.TaskId
import com.example.twoscreens.R
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import com.example.twoscreens.ui.helpers.Event
import com.example.twoscreens.ui.helpers.StateEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class TasksListViewModel(
    private val observeTasks: ObserveTasks,
    private val deleteTask: DeleteTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<TasksListViewState> {

    val stateStore = StateStore(TasksListViewState())

    var actualPaginationSize = 0L

    val onSuccessRemove = Event<Int>()

    init {
        runObserver()
    }

    override fun observeState() = stateStore.observe()

    private fun runObserver() {
        actualPaginationSize += PAGINATION_LIMIT_STEP

        scope.launch {
            observeTasks.invoke(
                paginationLimit = actualPaginationSize,
                response = { results ->
                    when (results) {
                        is Success -> stateStore.setState { copy(tasks = results.body) }
                        Error -> clearTasksList()
                    }
                })
        }
    }

    fun checkIfNeedToObserveMore() {
        if (stateStore.currentState.items.size >= actualPaginationSize)
            runObserver()
    }

    fun removeTask(id: TaskId) {
        val hasLastTaskBeforeRemove = stateStore.currentState.hasOnlyOneListElement
        scope.launch {
            deleteTask.invoke(id) { results ->
                if (results is Success) {
                    if (hasLastTaskBeforeRemove) clearTasksList()
                    onSuccessRemove.postEvent(R.string.removed)
                }
            }
        }
    }

    private fun clearTasksList() = stateStore.setState { copy(tasks = listOf()) }

}
