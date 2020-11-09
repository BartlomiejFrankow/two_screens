package com.example.twoscreens.ui.tasks.form

import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twoscreens.*
import com.example.twoscreens.ui.tasks.TaskItemDto
import com.example.twoscreens.ui.tasks.TasksListFragment
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
            if (areFieldsValid())
                model.createOrUpdateTask(title.text.toString(), description.text.toString(), iconUrl.text.toString())
        }

        iconUrl.addTextChangedListener(
            DebouncedTextWatcher(viewLifecycleOwner.lifecycleScope) { url ->
                if (isImageUrlValid()) {
                    iconLinkLayout.error = ""
                    preview.setImageUrl(url)
                }
            }
        )
    }

    private fun areFieldsValid(): Boolean {
        val isTitleValid = !title.text.isNullOrEmpty()
        val isDescriptionValid = !description.text.isNullOrEmpty()
        val isIconValid = iconUrl.text.isNullOrEmpty() || isImageUrlValid()

        if (isTitleValid) titleLayout.error = "" else titleLayout.error = getString(R.string.error_invalid_field)
        if (isDescriptionValid) descriptionLayout.error = "" else descriptionLayout.error = getString(R.string.error_invalid_field)
        if (isIconValid) iconLinkLayout.error = "" else iconLinkLayout.error = getString(R.string.error_invalid_url)

        return isTitleValid && isDescriptionValid && isIconValid
    }

    private fun isImageUrlValid() = URLUtil.isValidUrl(iconUrl.text.toString())

    private fun render(state: FormViewState) {
        submit.text = if (state.isEditMode) getString(R.string.edit) else getString(R.string.create)

        state.item?.let { item ->
            title.setText(item.title)
            description.setText(item.description)
            item.iconUrl?.let { url->
                iconUrl.setText(url)
                preview.setImageUrl(url)
            }
        }
    }

    companion object {
        private const val TODO_ITEM = "TODO_ITEM"
        private val FormFragment.item get() = requireArguments().getSerializable(TODO_ITEM)?.let { it as TaskItemDto }

        // TODO if will have some additional time...
        // I decide to move whole TaskItemDto because it's small. But if I will have more time then instead of moving whole dto I could create local db
        // and store tasks list to move only id between fragments. At Form fragment I could take this id and get object from local db.
        fun navigate(fragment: TasksListFragment, item: TaskItemDto? = null) {
            fragment.findNavController().navigate(R.id.action_todoList_to_form, bundleOf(TODO_ITEM to item))
        }

    }

}
