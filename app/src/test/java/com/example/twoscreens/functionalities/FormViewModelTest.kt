package com.example.twoscreens.functionalities

import com.example.domain.CreateTask
import com.example.domain.RequestResult
import com.example.domain.UpdateTask
import com.example.domain.dto.*
import com.example.twoscreens.R
import com.example.twoscreens.ui.tasks.form.FormViewModel
import com.example.twoscreens.ui.tasks.form.isEditMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.threeten.bp.Instant

@Suppress("ClassName")
@ExperimentalCoroutinesApi
@FlowPreview
class FormViewModelTest {

    private val createTask = object : CreateTask {
        override suspend fun invoke(title: Title, description: Description, iconUrl: ImageUrl?, response: (RequestResult<Unit>) -> Unit) {
            response(RequestResult.Success(Unit))
        }
    }

    private val updateTask = object : UpdateTask {
        override suspend fun invoke(id: TaskId, title: Title, description: Description, iconUrl: ImageUrl?, response: (RequestResult<Unit>) -> Unit) {
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
            TaskItemDto(TaskId("test_id_0"), Title("title_0"), Description("description_0"), ImageUrl("https://test_0.jpeg"), Instant.now()),
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
