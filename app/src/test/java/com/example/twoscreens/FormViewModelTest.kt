package com.example.twoscreens

import com.example.twoscreens.firebase.CreateTask
import com.example.twoscreens.firebase.RequestResult
import com.example.twoscreens.firebase.UpdateTask
import com.example.twoscreens.ui.tasks.TaskItemDto
import com.example.twoscreens.ui.tasks.form.FormViewModel
import com.example.twoscreens.ui.tasks.form.isEditMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.threeten.bp.Instant

@Suppress("ClassName")
@ExperimentalCoroutinesApi
class FormViewModelTest {

    private val createTask = object : CreateTask {
        override suspend fun invoke(title: String, description: String, iconUrl: String, response: (RequestResult<Unit>) -> Unit) {
            response(RequestResult.Success(Unit))
        }
    }

    private val updateTask = object : UpdateTask {
        override suspend fun invoke(id: String, title: String, description: String, iconUrl: String, response: (RequestResult<Unit>) -> Unit) {
            response(RequestResult.Success(Unit))
        }
    }

    @Nested
    inner class `on create task` {

        private val model = FormViewModel(
            null,
            createTask,
            updateTask,
            TestCoroutineScope()
        )

        @Test
        fun `check is create mode`() {
            assertThat(model.stateStore.currentState)
                .matches { state -> !state.isEditMode }
        }

        @Test
        fun `on success show message`() {
            // when user will fill fields and click on create button...
            model.createOrUpdateTask("Test", "Test", "https://test.jpeg")

            assertThat(model.onSuccess.lastValue)
                .matches { value -> value == R.string.task_created }
        }

    }

    @Nested
    inner class `on update task` {

        private val model = FormViewModel(
            TaskItemDto("test_id_0", "title_0", "description_0", "https://test_0.jpeg", Instant.now()),
            createTask,
            updateTask,
            TestCoroutineScope()
        )

        @Test
        fun `check is edit mode`() {
            assertThat(model.stateStore.currentState)
                .matches { state -> state.isEditMode }
        }

        @Test
        fun `on success show message`() {
            // when user will fill fields and click on edit button...
            model.createOrUpdateTask("Test", "Test", "https://test.jpeg")

            assertThat(model.onSuccess.lastValue)
                .matches { value -> value == R.string.task_updated }
        }

    }

}
