package com.example.twoscreens.ui.todo

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.twoscreens.R
import com.example.twoscreens.onEachState
import com.example.twoscreens.ui.todo.form.FormFragment
import kotlinx.android.synthetic.main.fragment_todo_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private val model: TodoListViewModel by viewModel()

    private val adapter = TodoListAdapter(
        onClick = { todoItemDto -> FormFragment.navigate(this, todoItemDto) },
        onLongClick = { askForDelete() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.onEachState(this, ::render)

        list.adapter = adapter

        addNew.setOnClickListener {
            FormFragment.navigate(this)
        }
    }

    private fun render(state: TodoListViewState) {
        adapter.submitList(state.items)
    }

    private fun askForDelete() {
        AlertDialog.Builder(context)
            .setMessage(R.string.delete_info)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
            .show()
    }


}
