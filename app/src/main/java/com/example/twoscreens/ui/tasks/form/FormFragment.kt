package com.example.twoscreens.ui.tasks.form

import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.dto.TaskItemDto
import com.example.twoscreens.R
import com.example.twoscreens.ui.helpers.*
import com.example.twoscreens.ui.tasks.TasksListFragment
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@FlowPreview
@ExperimentalCoroutinesApi
class FormFragment : Fragment(R.layout.fragment_form) {

    private val model: FormViewModel by viewModel { parametersOf(item) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.onEachState(this, ::render)
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
                setPreviewAndErrorState(url)
            }
        )
    }

    private fun setPreviewAndErrorState(url: String) {
        when {
            url.isEmpty() -> {
                iconLinkLayout.error = ""
                preview.setImageUrl(null)
            }
            isImageUrlValid(url) -> {
                iconLinkLayout.error = ""
                preview.setImageUrl(url)
            }
            !isImageUrlValid(url) -> {
                iconLinkLayout.error = getString(R.string.error_invalid_url)
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        val isTitleValid = !title.text.isNullOrEmpty()
        val isDescriptionValid = !description.text.isNullOrEmpty()
        val isIconValid = iconUrl.text.isNullOrEmpty() || isImageUrlValid(iconUrl.text.toString())

        if (isTitleValid) titleLayout.error = "" else titleLayout.error = getString(R.string.error_invalid_field)
        if (isDescriptionValid) descriptionLayout.error = "" else descriptionLayout.error = getString(R.string.error_invalid_field)

        return isTitleValid && isDescriptionValid && isIconValid
    }

    private fun isImageUrlValid(url: String) = URLUtil.isValidUrl(url)

    private fun render(state: FormViewState) {
        submit.text = if (state.isEditMode) getString(R.string.edit) else getString(R.string.create)

        state.item?.let { item ->
            title.setText(item.title.value)
            description.setText(item.description.value)
            item.iconUrl?.let { imageUrl ->
                iconUrl.setText(imageUrl.value)
                preview.setImageUrl(imageUrl.value)
            }
        }
    }

    companion object {
        private const val TODO_ITEM = "TODO_ITEM"
        private val FormFragment.item get() = requireArguments().getSerializable(TODO_ITEM)?.let { it as TaskItemDto }

        fun navigate(fragment: TasksListFragment, item: TaskItemDto? = null) {
            fragment.findNavController().navigate(R.id.action_todoList_to_form, bundleOf(TODO_ITEM to item))
        }

    }

}
