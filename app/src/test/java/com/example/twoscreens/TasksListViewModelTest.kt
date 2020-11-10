package com.example.twoscreens

import com.example.twoscreens.firebase.DeleteTask
import com.example.twoscreens.firebase.ObserveTasks
import com.example.twoscreens.firebase.RequestResult
import com.example.twoscreens.ui.tasks.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.threeten.bp.Instant

@Suppress("ClassName")
@ExperimentalCoroutinesApi
class TasksListViewModelTest {

    private val successObserveTasks = object : ObserveTasks {
        override suspend fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit) {
            response(RequestResult.Success(mockTasks(30)))
        }
    }

    private val emptyObserveTasks = object : ObserveTasks {
        override suspend fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit) {
            response(RequestResult.Success(listOf()))
        }
    }

    private val errorObserveTasks = object : ObserveTasks {
        override suspend fun invoke(paginationLimit: Long, response: (RequestResult<List<TaskItemDto>>) -> Unit) {
            response(RequestResult.Error)
        }
    }

    private val deleteTask = object : DeleteTask {
        override suspend fun invoke(id: String, response: (RequestResult<Unit>) -> Unit) {
            response(RequestResult.Success(Unit))
        }
    }

    @Nested
    inner class `after success empty response` {

        private val model = TasksListViewModel(
            emptyObserveTasks,
            deleteTask,
            TestCoroutineScope()
        )

        @Test
        fun `check is progress invisible after load empty data`() {
            // progress before request is visible as default
            assertThat(model.stateStore.currentState)
                .matches { state -> !state.showLoading }
        }

        @Test
        fun `check is empty list visible before load data`() {
            assertThat(model.stateStore.currentState)
                .matches { state -> state.showEmptyInfo }
        }

    }

    @Nested
    inner class `after error response` {

        private val model = TasksListViewModel(
            errorObserveTasks,
            deleteTask,
            TestCoroutineScope()
        )

        @Test
        fun `check is progress invisible after load empty data`() {
            // progress before request is visible as default
            assertThat(model.stateStore.currentState)
                .matches { state -> !state.showLoading }
        }

        @Test
        fun `check is empty list visible before load data`() {
            assertThat(model.stateStore.currentState)
                .matches { state -> state.showEmptyInfo }
        }

    }


    @Nested
    inner class `after success response` {

        private var firstPaginationChunk = 30L
        private var secondPaginationChunk = 60L

        private val model = TasksListViewModel(
            successObserveTasks,
            deleteTask,
            TestCoroutineScope()
        )

        @Test
        fun `check first page load pagination size`() {
            assertThat(model.actualPaginationSize)
                .matches { pagination ->
                    pagination == firstPaginationChunk
                }
        }

        @Test
        fun `check next page load pagination increase`() {
            // when user will scroll to bottom of list...
            model.checkIfNeedToObserveMore()

            assertThat(model.actualPaginationSize)
                .matches { pagination ->
                    pagination == secondPaginationChunk
                }
        }

        @Test
        fun `on next page load show progress`() {
            // when user will scroll to bottom of list...
            model.checkIfNeedToObserveMore()

            assertThat(model.showPaginationLoader.lastValue)
                .isNotNull
        }

        @Test
        fun `check is progress invisible after load data`() {

            assertThat(model.stateStore.currentState)
                .matches { state -> !state.showLoading }
        }

        @Test
        fun `check is there only one item left`() {
            model.stateStore.setState {
                copy(tasks = mockTasks(1))
            }

            assertThat(model.stateStore.currentState)
                .matches { state -> state.hasOnlyOneListElement }
        }

        @Test
        fun `on remove item show success message`() {
            model.removeTask("test_id_0")

            assertThat(model.onSuccessRemove.lastValue)
                .matches { value -> value == R.string.removed }
        }

    }

    private val date = Instant.now()

    private fun mockTasks(size: Int): List<TaskItemDto> {
        val tasks = mutableListOf<TaskItemDto>()
        for (position in 0 until size) {
            tasks.add(TaskItemDto("test_id_$position", "title_$position", "description_$position", "https://test_$position.jpeg", date))
        }
        return tasks.toList()
    }

}
