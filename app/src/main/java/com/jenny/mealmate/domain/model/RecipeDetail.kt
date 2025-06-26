package com.jenny.mealmate.domain.model

data class RecipeDetail(
    val id            : String,
    val title         : String,
    val category      : String,
    val instructions  : String,
    val images        : List<String>,
    val likes         : Int,
    val createdById   : String,
    val createdByName : String,
    val createdAt     : String,
    val ingredients   : List<IngredientDetail>
)

data class IngredientDetail(
    val id      : String,
    val name    : String,
    val image   : String,
    val quantity: String,
    val unit    : String
)
