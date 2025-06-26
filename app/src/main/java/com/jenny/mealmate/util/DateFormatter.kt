package com.jenny.mealmate.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDisplayDate(): String {
    return try {
        // parse the incoming text
        val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault())
        val date = LocalDate.parse(this, inputFormatter)

        // format to “d MMM yyyy”
        val outputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
        date.format(outputFormatter)
    } catch (e: Exception) {
        // if something goes wrong, just return the raw input
        this
    }
}