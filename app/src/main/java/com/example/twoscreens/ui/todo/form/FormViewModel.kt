package com.example.twoscreens.ui.todo.form

import com.example.twoscreens.StateEmitter
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import com.example.twoscreens.ui.todo.TodoItemDto
import kotlinx.coroutines.CoroutineScope

class FormViewModel(
    todoItemDto: TodoItemDto?,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<FormViewState> {

    private val stateStore = StateStore(FormViewState(todoItemDto, isEditMode = todoItemDto != null))

    override fun observeState() = stateStore.observe()

}
