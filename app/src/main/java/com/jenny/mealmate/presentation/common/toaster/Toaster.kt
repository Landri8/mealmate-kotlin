package com.jenny.mealmate.presentation.common.toaster

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object Toaster {
    private val _events = MutableSharedFlow<ToasterEvent>()
    val events = _events.asSharedFlow()

    suspend fun show(message: String) {
        _events.emit(ToasterEvent(message))
    }
}
