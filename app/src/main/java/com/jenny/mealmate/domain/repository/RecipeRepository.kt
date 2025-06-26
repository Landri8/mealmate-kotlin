package com.jenny.mealmate.domain.repository

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.domain.model.RecipeDetail
import com.jenny.mealmate.domain.usecases.recipe.SaveRecipeUseCase

interface RecipeRepository {
    suspend fun getRecipeDetail(id: String): ApiResponseDto<RecipeDetail>
    suspend fun saveRecipe(request: SaveRecipeUseCase.Request): ApiResponseDto<Unit>
    suspend fun deleteRecipe(recipeId: String): ApiResponseDto<Unit>
}
