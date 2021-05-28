package com.example.twoscreens.ui.tasks.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.dto.TaskItemDto
import com.example.twoscreens.R
import com.example.twoscreens.databinding.FragmentFormBinding
import com.example.twoscreens.ui.helpers.*
import com.example.twoscreens.ui.tasks.TasksListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@FlowPreview
@ExperimentalCoroutinesApi
class FormFragment : Fragment(R.layout.fragment_form) {

    private lateinit var binding: FragmentFormBinding

    private val model: FormViewModel by viewModel { parametersOf(item) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.onEachState(this, ::render)
        model.onSuccess.onEachEvent(this) { message ->
            showToast(message)
            findNavController().navigateUp()
        }

        binding.submit.setOnClickListener {
            if (areFieldsValid())
                model.createOrUpdateTask(binding.title.text.toString(), binding.description.text.toString(), binding.iconUrl.text.toString())
        }

        binding.iconUrl.addTextChangedListener(
            DebouncedTextWatcher(viewLifecycleOwner.lifecycleScope) { url ->
                setPreviewAndErrorState(url)
            }
        )
    }

    private fun setPreviewAndErrorState(url: String) {
        when {
            url.isEmpty() -> {
                binding.iconLinkLayout.error = ""
                binding.preview.setImageUrl(null)
            }
            isImageUrlValid(url) -> {
                binding.iconLinkLayout.error = ""
                binding.preview.setImageUrl(url)
            }
            !isImageUrlValid(url) -> {
                binding.iconLinkLayout.error = getString(R.string.error_invalid_url)
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        val isTitleValid = !binding.title.text.isNullOrEmpty()
        val isDescriptionValid = !binding.description.text.isNullOrEmpty()
        val isIconValid = binding.iconUrl.text.isNullOrEmpty() || isImageUrlValid(binding.iconUrl.text.toString())

        if (isTitleValid) binding.titleLayout.error = "" else binding.titleLayout.error = getString(R.string.error_invalid_field)
        if (isDescriptionValid) binding.descriptionLayout.error = "" else binding.descriptionLayout.error = getString(R.string.error_invalid_field)

        return isTitleValid && isDescriptionValid && isIconValid
    }

    private fun isImageUrlValid(url: String) = URLUtil.isValidUrl(url)

    private fun render(state: FormViewState) {
        binding.submit.text = if (state.isEditMode) getString(R.string.edit) else getString(R.string.create)

        state.item?.let { item ->
            binding.title.setText(item.title.value)
            binding.description.setText(item.description.value)
            item.iconUrl?.let { imageUrl ->
                binding.iconUrl.setText(imageUrl.value)
                binding.preview.setImageUrl(imageUrl.value)
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
