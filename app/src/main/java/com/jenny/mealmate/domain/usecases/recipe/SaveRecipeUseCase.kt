package com.jenny.mealmate.domain.usecases.recipe

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.domain.repository.RecipeRepository
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val repo: RecipeRepository
) {
    data class Request(
        val recipe_id: String,
        val title: String,
        val instructions: String,
        val images: List<String>,
        val ingredients: List<IngDto>,
        val category: String,
        val videoUrl: String?
    )
    data class IngDto(val ingredientId: String, val quantity: String, val unit: String)

    suspend operator fun invoke(request: Request): ApiResponseDto<Unit> =
        repo.saveRecipe(request)
}
