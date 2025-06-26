package com.jenny.mealmate.domain.manager

import com.jenny.mealmate.domain.model.RecipeDetail
import kotlinx.coroutines.flow.Flow

interface BookmarkManager {
    suspend fun toggleBookmark(recipeId: String)

    fun isBookmarked(recipeId: String): Flow<Boolean>
    suspend fun updateBookmark(recipe: RecipeDetail)
    fun getAllBookmarks(): Flow<List<RecipeDetail>>
}
