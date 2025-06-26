package com.jenny.mealmate.data.repository

import com.jenny.mealmate.data.remote.api.IngredientApi
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.model.Ingredient
import com.jenny.mealmate.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(
    private val api: IngredientApi,
    private val localUserManager: LocalUserManager
) : IngredientRepository {
    override suspend fun getIngredients(): List<Ingredient> {
        val token = localUserManager.readUser().first().token
        val bearer = "Bearer $token"

        return api.list(bearer).data
            .orEmpty()
            .map { it.toDomain() }
    }

}
