package com.example.twoscreens.ui.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.twoscreens.*
import com.example.twoscreens.ui.tasks.form.FormFragment
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TasksListFragment : Fragment(R.layout.fragment_tasks_list) {

    private val model: TasksListViewModel by viewModel()

    private val adapter = TodoListAdapter(
        onClick = { dto -> FormFragment.navigate(this, dto) },
        onLongClick = { dto -> askForDelete(dto.id) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(requireActivity())

        model.onEachState(this, ::render)
        model.doOnError.onEachEvent(this, ::showToast)
        model.onSuccessRemove.onEachEvent(this, ::showToast)

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

    private fun askForDelete(id: String) {
        AlertDialog.Builder(context)
            .setMessage(R.string.delete)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                model.removeTask(id)
                dialog.cancel()
            }
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
            .show()
    }

}
