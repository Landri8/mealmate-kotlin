package com.jenny.mealmate.domain.repository

import com.jenny.mealmate.domain.model.Ingredient

interface IngredientRepository {
    suspend fun getIngredients(): List<Ingredient>
}
