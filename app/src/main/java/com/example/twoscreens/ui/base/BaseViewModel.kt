package com.example.twoscreens.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

abstract class BaseViewModel(coroutineScope: CoroutineScope?) : ViewModel() {

    protected val scope: CoroutineScope = coroutineScope ?: viewModelScope
}
