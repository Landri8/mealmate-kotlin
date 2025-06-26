package com.jenny.mealmate.domain.model

data class CategorySection(
    val categoryId: String,
    val categoryName: String,
    val recipes: List<Recipe>
)