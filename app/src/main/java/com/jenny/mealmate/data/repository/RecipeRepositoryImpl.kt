package com.jenny.mealmate.data.repository

import android.util.Log
import com.jenny.mealmate.data.remote.api.RecipeApi
import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.RecipeDetailDto
import com.jenny.mealmate.data.remote.dto.SaveRecipeRequestDto
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.model.RecipeDetail
import com.jenny.mealmate.domain.repository.RecipeRepository
import com.jenny.mealmate.domain.usecases.recipe.SaveRecipeUseCase
import com.jenny.mealmate.util.ApiStatus
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val localUserManager: LocalUserManager
) : RecipeRepository {
    override suspend fun getRecipeDetail(recipeId: String): ApiResponseDto<RecipeDetail> {
        return try {
            val token = localUserManager.readUser().first().token
            val bearer = "Bearer $token"
            val resp: ApiResponseDto<RecipeDetailDto> = api.getRecipeDetail(bearer, recipeId)
            Log.d("API_DETAIL", "status=${resp.statusCode}")
            if (resp.statusCode == ApiStatus.SUCCESS.code && resp.data != null) {
                ApiResponseDto(
                    statusCode = resp.statusCode,
                    message = resp.message,
                    data = resp.data.toDomain()
                )
            } else {
                ApiResponseDto(ApiStatus.FAIL.code, resp.message, null)
            }
        } catch (e: Exception) {
            Log.e("API_DETAIL", "error", e)
            ApiResponseDto(ApiStatus.ERROR.code, "Something went wrong", null)
        }
    }

    override suspend fun saveRecipe(request: SaveRecipeUseCase.Request): ApiResponseDto<Unit> {
        return try {
            val token  = localUserManager.readUser().first().token
            val bearer = "Bearer $token"

            Log.d("API_DETAIL", "Fetching recipe detail ${request.recipe_id}")

            // map domain request → DTO
            val dto = SaveRecipeRequestDto(
                recipe_id    = request.recipe_id,
                title        = request.title,
                instructions = request.instructions,
                images       = request.images,
                ingredients  = request.ingredients.map {
                    SaveRecipeRequestDto.IngredientDto(it.ingredientId, it.quantity, it.unit)
                },
                category  = request.category,
                videoUrl  = request.videoUrl
            )

            // call API
            val resp = api.saveRecipe(bearer, dto)

            Log.d("API_DETAIL", "Response status=${resp.statusCode}, message=${resp.message}, data=${resp.data}")
            // drop resp.data and return Unit
            ApiResponseDto(resp.statusCode, resp.message, Unit)
        } catch (e: Exception) {
            Log.e("REPO_SAVE", "error saving recipe", e)
            ApiResponseDto(ApiStatus.ERROR.code, e.localizedMessage ?: "Unknown error", Unit)
        }
    }

    override suspend fun deleteRecipe(recipeId: String): ApiResponseDto<Unit> {
        val token = localUserManager.readUser().first().token
        val bearer = "Bearer $token"
        return api.deleteRecipe(bearer, recipeId)
    }
}
