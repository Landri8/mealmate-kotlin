package com.jenny.mealmate.data.remote.dto

import com.jenny.mealmate.domain.model.CreateOrEditRecipeRequest

data class CreateOrEditRecipeRequestDto(
    val recipe_id   : String,
    val title       : String,
    val instructions: String,
    val images      : List<String>,
    val ingredients : List<IngredientRequestDto>,
    val category    : String,
    val videoUrl    : String?
) {
    fun toDomain() = CreateOrEditRecipeRequest(
        recipe_id    = recipe_id,
        title        = title,
        instructions = instructions,
        images       = images,
        ingredients  = ingredients.map { it.toDomain() },
        category     = category,
        videoUrl     = videoUrl
    )
}

data class IngredientRequestDto(
    val ingredientId: String,
    val quantity    : String,
    val unit        : String
) {
    fun toDomain() = com.jenny.mealmate.domain.model.IngredientRequest(ingredientId, quantity, unit)
}
