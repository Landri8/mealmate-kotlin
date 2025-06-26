package com.jenny.mealmate.domain.model

data class CreateOrEditRecipeRequest(
    val recipe_id   : String,
    val title       : String,
    val instructions: String,
    val images      : List<String>,
    val ingredients : List<IngredientRequest>,
    val category    : String,
    val videoUrl    : String?
)


