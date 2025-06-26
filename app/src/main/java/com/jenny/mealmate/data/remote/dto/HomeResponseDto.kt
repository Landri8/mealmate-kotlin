package com.jenny.mealmate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HomeResponseDto(
    val statusCode: Int,
    val message: String,
    val data: HomeDataDto?
)

data class HomeDataDto(
    val userCreatedRecipes: List<RecipeDto>?,
    val recommendations:      List<RecipeDto>?,
    val categories:           List<CategoryDto>?
)

data class RecipeDto(
    val title:       String?,
    val category:    String?,
    val images:      List<String>?,
    val likes:       Int?,
    val createdBy:   String?,
    val id:          String?,
    val createdAt:   String?
)

data class CategoryDto(
    @SerializedName("category_id")   val categoryId:   String?,
    @SerializedName("category_name") val categoryName: String?,
    val recipes:                     List<RecipeDto>?
)
