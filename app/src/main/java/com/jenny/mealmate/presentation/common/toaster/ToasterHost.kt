package com.jenny.mealmate.presentation.common.toaster

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ToasterHost() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Toaster.events.collectLatest {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }
}