package com.example.twoscreens.ui.tasks.form

import com.example.domain.dto.TaskItemDto

data class FormViewState(var item: TaskItemDto?)

val FormViewState.isEditMode
        get() = item != null
