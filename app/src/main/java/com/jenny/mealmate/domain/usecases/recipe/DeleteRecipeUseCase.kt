// app/src/main/java/com/jenny/mealmate/domain/usecases/recipe/DeleteRecipeUseCase.kt
package com.jenny.mealmate.domain.usecases.recipe

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.domain.repository.RecipeRepository
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(
    private val repo: RecipeRepository
) {
    suspend operator fun invoke(recipeId: String): ApiResponseDto<Unit> =
        repo.deleteRecipe(recipeId)
}
