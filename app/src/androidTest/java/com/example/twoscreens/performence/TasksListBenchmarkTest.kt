package com.example.twoscreens.performence

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.twoscreens.R
import com.example.twoscreens.ui.tasks.TasksListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test


class TasksListBenchmarkTest {

    @FlowPreview
    @ExperimentalCoroutinesApi
    val scenario = FragmentScenario.launchInContainer(TasksListFragment::class.java)

    @Test
    fun checkListRendering() {
        val listRenderingCount = 100

        for (i in 0..listRenderingCount) {
            onView(withId(R.id.list))
        }
    }

}