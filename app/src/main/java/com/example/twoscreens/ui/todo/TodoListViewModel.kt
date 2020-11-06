package com.example.twoscreens.ui.todo

import com.example.twoscreens.StateEmitter
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import kotlinx.coroutines.CoroutineScope
import org.threeten.bp.Instant

class TodoListViewModel(coroutineScope: CoroutineScope? = null) : BaseViewModel(coroutineScope), StateEmitter<TodoListViewState> {

    private val stateStore = StateStore(TodoListViewState())

    init {
        val desc = "I searched for a possible solution but couldn't find anything useful: one solution suggested I need to use another jar that includes the time-zone rules and other suggested that there might be two or more ThreeTenBP-libraries inside the classpath."
        val date = Instant.now()
        val mockedTodoList = listOf(
            TodoItemDto(1, "awesome 1", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(2, "awesome 2", desc, null, Instant.now()),
            TodoItemDto(3, "awesome 3", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(4, "awesome 4", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(5, "awesome 5", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(6, "awesome 6", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(7, "awesome 7", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(8, "awesome 8", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date),
            TodoItemDto(9, "awesome 9", desc, "https://miro.medium.com/max/256/1*d69DKqFDwBZn_23mizMWcQ.png", date)
        )

        stateStore.setState { copy(items = mockedTodoList) }
    }

    override fun observeState() = stateStore.observe()

}
