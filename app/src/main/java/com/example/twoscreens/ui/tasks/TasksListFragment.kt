package com.example.twoscreens.ui.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twoscreens.*
import com.example.twoscreens.ui.tasks.form.FormFragment
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TasksListFragment : Fragment(R.layout.fragment_tasks_list) {

    private val model: TasksListViewModel by viewModel()

    private var isScrolling = false

    private val adapter = TodoListAdapter(
        onClick = { dto -> FormFragment.navigate(this, dto) },
        onLongClick = { dto -> askForDelete(dto.id) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(requireActivity())

        model.onEachState(this, ::render)
        model.onSuccessRemove.onEachEvent(this, ::showToast)

        list.adapter = adapter

        addNew.setOnClickListener {
            FormFragment.navigate(this)
        }

        paginationListener()
    }

    private fun paginationListener() {
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                    model.checkIfNeedToIncreasePagination()
                }
            }
        })
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
