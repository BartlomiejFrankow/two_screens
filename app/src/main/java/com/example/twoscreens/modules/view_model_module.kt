package com.example.twoscreens.modules

import com.example.twoscreens.firebase.ErrorMessage
import com.example.twoscreens.ui.SnackBarError
import com.example.twoscreens.ui.tasks.TaskItemDto
import com.example.twoscreens.ui.tasks.TasksListViewModel
import com.example.twoscreens.ui.tasks.form.FormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TasksListViewModel(get(), get()) }
    viewModel { (taskItemDto: TaskItemDto) -> FormViewModel(taskItemDto, get(), get()) }

    single { SnackBarError() }
    factory<ErrorMessage> { get<SnackBarError>() }
}
