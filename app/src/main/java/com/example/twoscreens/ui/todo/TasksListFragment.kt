package com.example.twoscreens.ui.todo

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.twoscreens.R
import com.example.twoscreens.hideSoftKeyboard
import com.example.twoscreens.onEachEvent
import com.example.twoscreens.onEachState
import com.example.twoscreens.ui.todo.form.FormFragment
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TasksListFragment : Fragment(R.layout.fragment_tasks_list) {

    private val model: TasksListViewModel by viewModel()

    private val adapter = TodoListAdapter(
        onClick = { todoItemDto -> FormFragment.navigate(this, todoItemDto) },
        onLongClick = { askForDelete() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(requireActivity())

        model.onEachState(this, ::render)

        model.doOnError.onEachEvent(this) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        list.adapter = adapter

        addNew.setOnClickListener {
            FormFragment.navigate(this)
        }
    }

    private fun render(state: TasksListViewState) {
        emptyView.isVisible = state.showEmptyInfo
        loading.isVisible = state.showLoading
        adapter.submitList(state.items)
    }

    private fun askForDelete() {
        AlertDialog.Builder(context)
            .setMessage(R.string.delete)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
            .show()
    }


}
