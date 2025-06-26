package com.jenny.mealmate.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.jenny.mealmate.domain.manager.BookmarkManager
import com.jenny.mealmate.domain.model.RecipeDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "bookmark_prefs")
private object Keys {
    val BOOKMARKS = booleanPreferencesKey("bookmarked_recipes")
}

class BookmarkManagerImpl @Inject constructor(
    private val context: Context,
    private val prefs: DataStore<Preferences>
) : BookmarkManager {
    private val gson = Gson()

    override suspend fun toggleBookmark(recipeId: String) {
        prefs.edit { store ->
            val flagKey = booleanPreferencesKey("flag_$recipeId")
            val jsonKey = stringPreferencesKey("bookmark_$recipeId")

            val nowBookmarked = !(store[flagKey] ?: false)
            store[flagKey] = nowBookmarked

            if (!nowBookmarked) {
                // removed bookmark → also delete the saved JSON
                store.remove(jsonKey)
            }
        }
    }

    override suspend fun updateBookmark(recipe: RecipeDetail) {
        // example: serialize RecipeDetail to JSON and re‐save into your DataStore or DB
        prefs.edit { store ->
            val key = stringPreferencesKey("bookmark_${recipe.id}")
            store[key] = gson.toJson(recipe)
        }
    }

    override fun isBookmarked(recipeId: String): Flow<Boolean> =
        prefs.data.map { it[booleanPreferencesKey("flag_$recipeId")] ?: false }

    override fun getAllBookmarks(): Flow<List<RecipeDetail>> =
        prefs.data.map { store ->
            store.asMap().mapNotNull { (key, value) ->
                // look only at keys named "bookmark_<id>"
                if (key.name.startsWith("bookmark_") && value is String) {
                    try {
                        gson.fromJson(value, RecipeDetail::class.java)
                    } catch (_: Exception) {
                        null
                    }
                } else null
            }
        }
}
