package com.example.twoscreens

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebouncedTextWatcher(
    private val scope: CoroutineScope,
    private val action: suspend (String) -> Unit
) : TextWatcher {

    private var job = null as Job?

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        job?.cancel()
        job = scope.launch {
            delay(300)
            action(text.toString())
        }
    }
}
