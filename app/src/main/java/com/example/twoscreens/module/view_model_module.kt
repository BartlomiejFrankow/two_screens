package com.example.twoscreens.module

import com.example.twoscreens.ui.todo.TodoItemDto
import com.example.twoscreens.ui.todo.TodoListViewModel
import com.example.twoscreens.ui.todo.form.FormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TodoListViewModel() }
    viewModel { (todoItemDto: TodoItemDto) -> FormViewModel(todoItemDto) }
}
