package com.example.twoscreens.modules

import com.example.twoscreens.ui.todo.TaskItemDto
import com.example.twoscreens.ui.todo.TasksListViewModel
import com.example.twoscreens.ui.todo.form.FormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TasksListViewModel(get()) }
    viewModel { (taskItemDto: TaskItemDto) -> FormViewModel(taskItemDto, get(), get()) }
}
