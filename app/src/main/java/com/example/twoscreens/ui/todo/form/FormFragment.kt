package com.example.twoscreens.ui.todo.form

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twoscreens.R
import com.example.twoscreens.onEachEvent
import com.example.twoscreens.onEachState
import com.example.twoscreens.ui.todo.TodoItemDto
import com.example.twoscreens.ui.todo.TodoListFragment
import kotlinx.android.synthetic.main.fragment_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FormFragment : Fragment(R.layout.fragment_form) {

    private val model: FormViewModel by viewModel { parametersOf(item) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.onEachState(this, ::render)

        model.onFireStoreFailed.onEachEvent(this) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        model.onFireStoreSuccess.onEachEvent(this) {
            Toast.makeText(requireContext(), R.string.fire_store_create_task_success, Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }

        submit.setOnClickListener {
            // TODO do validations
            model.sendTaskToFirebase(title.text.toString(), description.text.toString(), iconUrl.text.toString())
        }
    }

    private fun render(state: FormViewState) {
        submit.text = if (state.isEditMode) getString(R.string.edit) else getString(R.string.create)

        state.item?.let { item ->
            title.setText(item.title)
            description.setText(item.description)
            iconUrl.setText(item.iconUrl)
        }
    }

    companion object {
        private const val TODO_ITEM = "TODO_ITEM"
        private val FormFragment.item get() = requireArguments().getSerializable(TODO_ITEM)?.let { it as TodoItemDto }

        // TODO if will have some additional time...
        // I decide to move whole TodoItemDto because it's small. But if I will have more time then instead of moving dto I could create local db
        // and store whole list to move only id between fragments. At Form fragment I could take this id and get object from local db.
        fun navigate(fragment: TodoListFragment, item: TodoItemDto? = null) {
            fragment.findNavController().navigate(R.id.action_todoList_to_form, bundleOf(TODO_ITEM to item))
        }

    }

}
