package com.example.twoscreens

import com.example.domain.ErrorMessage
import com.example.domain.dto.TaskItemDto
import com.example.twoscreens.ui.SnackBarError
import com.example.twoscreens.ui.tasks.TasksListViewModel
import com.example.twoscreens.ui.tasks.form.FormViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { TasksListViewModel(get(), get()) }
    viewModel { (taskItemDto: TaskItemDto) -> FormViewModel(taskItemDto, get(), get()) }

    single { SnackBarError() }
    factory<ErrorMessage> { get<SnackBarError>() }
}
