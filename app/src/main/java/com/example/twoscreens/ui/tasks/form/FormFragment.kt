package com.example.twoscreens.ui.tasks.form

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twoscreens.R
import com.example.twoscreens.onEachEvent
import com.example.twoscreens.onEachState
import com.example.twoscreens.showToast
import com.example.twoscreens.ui.tasks.TaskItemDto
import com.example.twoscreens.ui.tasks.TasksListFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FormFragment : Fragment(R.layout.fragment_form) {

    private val model: FormViewModel by viewModel { parametersOf(item) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.onEachState(this, ::render)
        model.onError.onEachEvent(this, ::showToast)
        model.onSuccess.onEachEvent(this) { message ->
            showToast(message)
            findNavController().navigateUp()
        }

        submit.setOnClickListener {
            if (areFieldsValid(title, description))
                model.createOrUpdateTask(title.text.toString(), description.text.toString(), iconUrl.text.toString())
        }
    }

    private fun areFieldsValid(title: TextInputEditText, description: TextInputEditText): Boolean {
        val isTitleValid = !title.text.isNullOrEmpty()
        val isDescriptionValid = !description.text.isNullOrEmpty()

        if (isTitleValid) titleLayout.error = "" else titleLayout.error = getString(R.string.error_invalid_field)
        if (isDescriptionValid) descriptionLayout.error = "" else descriptionLayout.error = getString(R.string.error_invalid_field)

        return isTitleValid && isDescriptionValid
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
        private val FormFragment.item get() = requireArguments().getSerializable(TODO_ITEM)?.let { it as TaskItemDto }

        // TODO if will have some additional time...
        // I decide to move whole TaskItemDto because it's small. But if I will have more time then instead of moving dto I could create local db
        // and store whole list to move only id between fragments. At Form fragment I could take this id and get object from local db.
        fun navigate(fragment: TasksListFragment, item: TaskItemDto? = null) {
            fragment.findNavController().navigate(R.id.action_todoList_to_form, bundleOf(TODO_ITEM to item))
        }

    }

}
