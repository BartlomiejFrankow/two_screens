package com.example.twoscreens.ui.tasks.form

import com.example.twoscreens.Event
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.CreateTask
import com.example.twoscreens.firebase.UpdateTask
import com.example.twoscreens.firebase.responses.CreateOrUpdateTaskResponse.Error
import com.example.twoscreens.firebase.responses.CreateOrUpdateTaskResponse.Success
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import com.example.twoscreens.ui.tasks.TaskItemDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FormViewModel(
    taskItemDto: TaskItemDto?,
    private val createTask: CreateTask,
    private val updateTask: UpdateTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<FormViewState> {

    private val stateStore = StateStore(FormViewState(taskItemDto, isEditMode = taskItemDto != null))

    override fun observeState() = stateStore.observe()

    val onError = Event<String>()
    val onSuccess = Event<Int>()

    fun createOrUpdateTask(title: String, description: String, iconUrl: String) {
        when (stateStore.currentState.isEditMode) {
            true -> scope.launch { update(title, description, iconUrl) }
            false -> scope.launch { create(title, description, iconUrl) }
        }
    }

    private suspend fun create(title: String, description: String, iconUrl: String) {
        createTask.invoke(title, description, iconUrl, response = { results ->
            when (results) {
                is Success -> onSuccess.postEvent(results.message)
                is Error -> onError.postEvent(results.message)
            }
        })
    }

    private suspend fun update(title: String, description: String, iconUrl: String) {
        updateTask.invoke(stateStore.currentState.item!!.id, title, description, iconUrl, response = { results ->
            when (results) {
                is Success -> onSuccess.postEvent(results.message)
                is Error -> onError.postEvent(results.message)
            }
        })
    }

}
