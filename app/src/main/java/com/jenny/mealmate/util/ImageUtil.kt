// File: app/src/main/java/com/jenny/mealmate/util/ImageUtils.kt
package com.jenny.mealmate.util

import android.content.Context
import android.net.Uri
import android.util.Base64

object ImageUtils {
    /**
     * Turn a content:// Uri (e.g. from the gallery) into a `data:image/<type>;base64,...` string.
     * Throws if it can’t open the stream.
     */
    fun uriToBase64DataUri(ctx: Context, uri: Uri): String {
        val cr = ctx.contentResolver
        val mime = cr.getType(uri) ?: "image/*"
        val bytes = cr.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalArgumentException("Cannot open URI: $uri")
        val b64   = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return "data:$mime;base64,$b64"
    }
}
