package com.jenny.mealmate.domain.usecases.recipe

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.domain.model.RecipeDetail
import com.jenny.mealmate.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeDetailUseCase @Inject constructor(
    private val repo: RecipeRepository
) {
    suspend operator fun invoke(recipeId: String): ApiResponseDto<RecipeDetail> =
        repo.getRecipeDetail(recipeId)
}
