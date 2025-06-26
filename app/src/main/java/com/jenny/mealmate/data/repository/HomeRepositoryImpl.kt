package com.jenny.mealmate.data.repository

import android.util.Log
import com.jenny.mealmate.data.remote.api.HomeApi
import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.HomeDataDto
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.model.CategorySection
import com.jenny.mealmate.domain.model.HomeData
import com.jenny.mealmate.domain.model.Recipe
import com.jenny.mealmate.domain.repository.HomeRepository
import com.jenny.mealmate.util.ApiStatus
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val localUserManager: LocalUserManager
) : HomeRepository {

    override suspend fun fetchHomeData(userId: String): ApiResponseDto<HomeData> {
        return try {
            Log.d("API_HOME", "Fetching home data for user=$userId")

            // 1) Read token
            val token = localUserManager.readUser().first().token
            val bearer = "Bearer $token"

            // 2) Call API
            val resp: ApiResponseDto<HomeDataDto> = api.getHomeData(bearer, mapOf("user_id" to userId))

            // 3) Check status & data
            if (resp.statusCode != ApiStatus.SUCCESS.code || resp.data == null) {
                Log.e("API_HOME", "Fetch failed: ${resp.message}")
                return ApiResponseDto(
                    statusCode = resp.statusCode,
                    message    = resp.message,
                    data       = HomeData(emptyList(), emptyList(), emptyList())
                )
            }

            // 4) Map DTO → domain
            val dto = resp.data!!
            val userCreated = dto.userCreatedRecipes.orEmpty().map { r ->
                Recipe(
                    id       = r.id.orEmpty(),
                    title    = r.title.orEmpty(),
                    imageUrl = r.images?.firstOrNull().orEmpty()
                )
            }
            val recommendations = dto.recommendations.orEmpty().map { r ->
                Recipe(
                    id       = r.id.orEmpty(),
                    title    = r.title.orEmpty(),
                    imageUrl = r.images?.firstOrNull().orEmpty()
                )
            }
            val categories = dto.categories.orEmpty().map { c ->
                CategorySection(
                    categoryId   = c.categoryId.orEmpty(),
                    categoryName = c.categoryName.orEmpty(),
                    recipes      = c.recipes.orEmpty().map { r ->
                        Recipe(
                            id       = r.id.orEmpty(),
                            title    = r.title.orEmpty(),
                            imageUrl = r.images?.firstOrNull().orEmpty()
                        )
                    }
                )
            }

            Log.d("API_HOME", "Home data fetched successfully")
            ApiResponseDto(
                statusCode = resp.statusCode,
                message    = resp.message,
                data       = HomeData(userCreated, recommendations, categories)
            )

        } catch (e: Exception) {
            Log.e("API_HOME", "Exception fetching home data", e)
            ApiResponseDto(
                statusCode = ApiStatus.ERROR.code,
                message    = "Something went wrong!",
                data       = HomeData(emptyList(), emptyList(), emptyList())
            )
        }
    }
}
