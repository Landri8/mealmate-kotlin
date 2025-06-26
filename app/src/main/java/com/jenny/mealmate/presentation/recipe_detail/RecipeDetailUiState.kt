package com.jenny.mealmate.presentation.recipe_detail

import com.jenny.mealmate.domain.model.RecipeDetail

data class RecipeDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val detail: RecipeDetail? = null,
    val isBookmarked: Boolean = false,
    val currentUserId: String = ""
)