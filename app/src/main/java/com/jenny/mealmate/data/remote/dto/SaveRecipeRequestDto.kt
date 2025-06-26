package com.jenny.mealmate.data.remote.dto

data class SaveRecipeRequestDto(
    val recipe_id   : String,
    val title       : String,
    val instructions: String,
    val images      : List<String>,
    val ingredients : List<IngredientDto>,
    val category    : String,
    val videoUrl    : String?
) {
    data class IngredientDto(
        val ingredientId: String,
        val quantity    : String,
        val unit        : String
    )
}
