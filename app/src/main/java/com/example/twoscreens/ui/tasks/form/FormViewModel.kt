package com.example.twoscreens.ui.tasks.form

import com.example.domain.CreateTask
import com.example.domain.RequestResult.Success
import com.example.domain.UpdateTask
import com.example.domain.dto.Description
import com.example.domain.dto.ImageUrl
import com.example.domain.dto.TaskItemDto
import com.example.domain.dto.Title
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
class FormViewModel(
    taskItemDto: TaskItemDto?,
    private val createTask: CreateTask,
    private val updateTask: UpdateTask,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<FormViewState> {

    val stateStore = StateStore(FormViewState(taskItemDto))

    override fun observeState() = stateStore.observe()

    val onSuccess = Event<Int>()

    fun createOrUpdateTask(title: String, description: String, iconUrl: String) {
        when (stateStore.currentState.isEditMode) {
            true -> scope.launch { update(Title(title), Description(description), ImageUrl(iconUrl).takeIf { iconUrl.isNotEmpty() }) }
            false -> scope.launch { create(Title(title), Description(description), ImageUrl(iconUrl).takeIf { iconUrl.isNotEmpty() }) }
        }
    }

    private suspend fun create(title: Title, description: Description, imageUrl: ImageUrl?) {
        createTask.invoke(title, description, imageUrl, response = { results ->
            if (results is Success) onSuccess.postEvent(R.string.task_created)
        })
    }

    private suspend fun update(title: Title, description: Description, imageUrl: ImageUrl?) {
        updateTask.invoke(stateStore.currentState.item!!.id, title, description, imageUrl, response = { results ->
                if (results is Success) onSuccess.postEvent(R.string.task_updated)
            })
    }

}
