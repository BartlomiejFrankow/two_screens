package com.example.twoscreens.ui.tasks.form

import com.example.twoscreens.Event
import com.example.twoscreens.R
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.firebase.CreateTask
import com.example.twoscreens.firebase.UpdateTask
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import com.example.twoscreens.ui.tasks.TaskItemDto
import kotlinx.coroutines.CoroutineScope

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
            true -> {
                updateTask.invoke(stateStore.currentState.item!!.id, title, description, iconUrl)
                    .addOnSuccessListener { onSuccess.postEvent(R.string.task_updated) }
                    .addOnFailureListener { exception -> exception.message?.let { onError.postEvent(it) } }
            }
            false -> {
                createTask.invoke(title, description, iconUrl)
                    .addOnSuccessListener { onSuccess.postEvent(R.string.task_created) }
                    .addOnFailureListener { exception -> exception.message?.let { onError.postEvent(it) } }
            }
        }
    }

}
