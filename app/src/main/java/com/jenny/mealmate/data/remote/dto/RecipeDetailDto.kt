package com.jenny.mealmate.data.remote.dto

import com.jenny.mealmate.domain.model.IngredientDetail
import com.jenny.mealmate.domain.model.RecipeDetail

data class RecipeDetailDto(
    val id           : String,
    val title        : String,
    val category     : String,
    val instructions : String,
    val images       : List<String>,
    val likes        : Int,
    val createdBy    : CreatorDto,
    val createdAt    : String,
    val ingredients  : List<RecipeIngredientDto>
) {
    fun toDomain() = RecipeDetail(
        id             = id,
        title          = title,
        category       = category,
        instructions   = instructions,
        images         = images,
        likes          = likes,
        createdById    = createdBy.id,
        createdByName  = createdBy.name,
        createdAt      = createdAt,
        ingredients    = ingredients.map {
            IngredientDetail(
                id       = it.id,
                name     = it.name,
                image    = it.image,
                quantity = it.quantity,
                unit     = it.unit
            )
        }
    )
}

data class CreatorDto(
    val id   : String,
    val name : String,
    val email: String
)

data class RecipeIngredientDto(
    val id      : String,
    val name    : String,
    val image   : String,
    val quantity: String,
    val unit    : String
)
