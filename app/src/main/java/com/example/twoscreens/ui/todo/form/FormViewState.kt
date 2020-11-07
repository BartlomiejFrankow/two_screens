package com.example.twoscreens.ui.todo.form

import com.example.twoscreens.ui.todo.TaskItemDto

data class FormViewState(
    var item: TaskItemDto?,
    val isEditMode: Boolean
)