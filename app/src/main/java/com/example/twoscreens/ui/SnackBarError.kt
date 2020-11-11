package com.example.twoscreens.ui

import android.graphics.Color
import com.example.domain.ErrorMessage
import com.example.twoscreens.ui.helpers.Event
import com.example.twoscreens.ui.helpers.onEachEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class SnackBarError : ErrorMessage {

    private val errorStringMessage = Event<String>()
    private val errorResMessage = Event<Int>()

    @FlowPreview
    fun registerActivity(owner: MainActivity) {
        errorStringMessage.onEachEvent(owner) { message ->
            showSnackBar(owner, message)
        }
        errorResMessage.onEachEvent(owner) { resource ->
            showSnackBar(owner, owner.getString(resource))
        }
    }

    private fun showSnackBar(activity: MainActivity, text: String) {
        Snackbar.make(
            activity.window.decorView.findViewById(android.R.id.content),
            text,
            Snackbar.LENGTH_INDEFINITE
        )
            .setBackgroundTint(Color.WHITE)
            .setActionTextColor(Color.RED)
            .apply { setAction("Close") { dismiss() } }
            .show()
    }

    @ExperimentalCoroutinesApi
    override fun show(message: String?) = errorStringMessage.postEvent(message ?: "Unrecognized error")
    @ExperimentalCoroutinesApi
    override fun show(resource: Int) = errorResMessage.postEvent(resource)
}
