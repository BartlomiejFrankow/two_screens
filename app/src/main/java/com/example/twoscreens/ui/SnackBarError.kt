package com.example.twoscreens.ui

import android.graphics.Color
import com.example.twoscreens.Event
import com.example.twoscreens.firebase.ErrorMessage
import com.example.twoscreens.onEachEvent
import com.google.android.material.snackbar.Snackbar

class SnackBarError : ErrorMessage {

    private val errorMessage = Event<String>()

    fun registerActivity(owner: MainActivity) =
        errorMessage.onEachEvent(owner) { message ->
            showSnackBar(owner, message)
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

    override fun show(message: String) = errorMessage.postEvent(message)
}
