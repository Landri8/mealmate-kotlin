package com.jenny.mealmate.presentation.home

import com.jenny.mealmate.domain.model.CategorySection
import com.jenny.mealmate.domain.model.Recipe


data class HomeUiState(
    val firstName: String = "",
    val recipes: List<Recipe> = emptyList(),
    val recommendations: List<Recipe> = emptyList(),
    val categories: List<CategorySection> = emptyList(),
    val selectedCategory: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)