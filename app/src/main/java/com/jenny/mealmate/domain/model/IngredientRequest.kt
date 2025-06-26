package com.jenny.mealmate.domain.model


data class IngredientRequest(
    val ingredientId: String,
    val quantity    : String,
    val unit        : String
)
