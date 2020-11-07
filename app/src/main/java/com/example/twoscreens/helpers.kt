package com.example.twoscreens

import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

interface StateEmitter<STATE> {
    fun observeState(): Flow<STATE>
}

suspend fun <STATE> StateEmitter<STATE>.currentState() = observeState().first()

fun <STATE> StateEmitter<STATE>.onEachState(fragment: Fragment, consumer: (STATE) -> Unit) {
    fragment.viewLifecycleOwner.lifecycleScope.launch {
        observeState()
            .collect { state -> consumer(state) }
    }
}

fun ImageView.setIconUrl(url: String?) = Picasso.get()
    .load(url)
    .placeholder(R.drawable.placeholder)
    .into(this, object : Callback {
        override fun onSuccess() {}

        override fun onError(e: Exception) {
            Log.e("ERROR", "$e: Error setting image URL: $url")
        }
    })

fun <EVENT : Any> Event<EVENT>.onEachEvent(fragment: Fragment, observer: (EVENT) -> Unit) =
    onEachEvent(
        launchWhenStarted = { action ->
            fragment
                .viewLifecycleOwner
                .lifecycleScope
                .launchWhenStarted { action() }
        },
        observer = observer
    )

fun hideSoftKeyboard(activity: Activity) {
    activity.getSystemService<InputMethodManager>()!!
        .hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun Instant.formatDate(pattern: String = "d MMMM, YYYY"): String = this.atZone(ZoneId.systemDefault())
    .format(DateTimeFormatter.ofPattern(pattern))
