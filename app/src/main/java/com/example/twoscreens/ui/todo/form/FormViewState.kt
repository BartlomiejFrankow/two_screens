package com.example.twoscreens.ui.todo.form

import com.example.twoscreens.ui.todo.TodoItemDto

data class FormViewState(
    var item: TodoItemDto?,
    val isEditMode: Boolean
)