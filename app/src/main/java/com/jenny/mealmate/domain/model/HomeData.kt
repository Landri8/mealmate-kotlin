package com.jenny.mealmate.domain.model

data class HomeData(
    val userCreatedRecipes: List<Recipe>,
    val recommendations: List<Recipe>,
    val categories: List<CategorySection>
)