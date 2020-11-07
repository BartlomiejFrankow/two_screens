package com.example.twoscreens.ui.todo.form

import com.example.twoscreens.Event
import com.example.twoscreens.StateEmitter
import com.example.twoscreens.ui.base.BaseViewModel
import com.example.twoscreens.ui.base.StateStore
import com.example.twoscreens.ui.todo.TodoItemDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope

class FormViewModel(
    todoItemDto: TodoItemDto?,
    coroutineScope: CoroutineScope? = null
) : BaseViewModel(coroutineScope), StateEmitter<FormViewState> {

    private val stateStore = StateStore(FormViewState(todoItemDto, isEditMode = todoItemDto != null))

    override fun observeState() = stateStore.observe()

    val onFireStoreFailed = Event<String>()
    val onFireStoreSuccess = Event<Unit>()

    fun sendTaskToFirebase(title: String, description: String, iconUrl: String) {
        val fireStore = FirebaseFirestore.getInstance()

        val task: MutableMap<String, Any> = HashMap()
        task["title"] = title
        task["description"] = description
        task["icon"] = iconUrl
        task["creationDate"] = Timestamp.now()

        fireStore.collection("tasks").add(task)
            .addOnSuccessListener {
                onFireStoreSuccess.postEvent(Unit)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { onFireStoreFailed.postEvent(it) }
            }
    }

}
