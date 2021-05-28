package com.example.twoscreens.ui.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.TaskId
import com.example.twoscreens.R
import com.example.twoscreens.databinding.FragmentTasksListBinding
import com.example.twoscreens.ui.helpers.hideSoftKeyboard
import com.example.twoscreens.ui.helpers.onEachEvent
import com.example.twoscreens.ui.helpers.onEachState
import com.example.twoscreens.ui.helpers.showToast
import com.example.twoscreens.ui.tasks.form.FormFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@FlowPreview
class TasksListFragment : Fragment(R.layout.fragment_tasks_list) {

    private lateinit var binding: FragmentTasksListBinding

    private val model: TasksListViewModel by viewModel()

    private var isScrolling = false

    private val adapter = TodoListAdapter(
        onClick = { dto -> FormFragment.navigate(this, dto) },
        onLongClick = { dto -> askForDelete(dto.id) }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTasksListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(requireActivity())

        model.onEachState(this, ::render)
        model.onSuccessRemove.onEachEvent(this, ::showToast)

        binding.list.adapter = adapter

        binding.addNew.setOnClickListener {
            FormFragment.navigate(this)
        }

        paginationListener()
    }

    private fun paginationListener() {
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)!!
                val isLastItemVisible = (lm.findFirstVisibleItemPosition() + lm.childCount == lm.itemCount)

                if (isScrolling && isLastItemVisible) {
                    isScrolling = false
                    model.checkIfNeedToObserveMore()
                }
            }
        })
    }

    private fun render(state: TasksListViewState) {
        binding.emptyView.isVisible = state.showEmptyInfo
        binding.loading.isVisible = state.showLoading
        adapter.submitList(state.items)
    }

    private fun askForDelete(id: TaskId) {
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
