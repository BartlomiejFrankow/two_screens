package com.example.twoscreens.ui.tasks.form

import com.example.twoscreens.ui.tasks.TaskItemDto

data class FormViewState(
    var item: TaskItemDto?,
    val isEditMode: Boolean
)