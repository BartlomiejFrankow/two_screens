package com.example.twoscreens.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.dto.TaskItemDto
import com.example.twoscreens.R
import com.example.twoscreens.ui.helpers.formatDate
import com.example.twoscreens.ui.helpers.setImageUrl
import kotlinx.android.synthetic.main.item_to_do.view.*

class TodoListAdapter(val onLongClick: (TaskItemDto) -> Unit, val onClick: (TaskItemDto) -> Unit) :
    ListAdapter<TaskItemDto, RecyclerView.ViewHolder>(TodoItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_to_do, parent, false)
            .let { object : RecyclerView.ViewHolder(it) {} }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder.itemView) {
            val item = getItem(position)

            title.text = item.title.value
            description.text = item.description.value
            icon.setImageUrl(item.iconUrl?.value)
            date.text = item.creationDate.formatDate()

            setOnClickListener {
                onClick(item)
            }
            setOnLongClickListener {
                onLongClick(item)
                true
            }
        }
    }
}

internal class TodoItemCallBack : DiffUtil.ItemCallback<TaskItemDto>() {
    override fun areItemsTheSame(oldItemDto: TaskItemDto, newItemDto: TaskItemDto) = oldItemDto == newItemDto
    override fun areContentsTheSame(oldItemDto: TaskItemDto, newItemDto: TaskItemDto) = oldItemDto == newItemDto
}
