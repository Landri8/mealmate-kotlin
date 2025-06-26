package com.jenny.mealmate.domain.usecases.ingredient

import com.jenny.mealmate.domain.model.Ingredient
import com.jenny.mealmate.domain.repository.IngredientRepository
import javax.inject.Inject

class GetIngredientsUseCase @Inject constructor(
    private val repo: IngredientRepository
) {
    suspend operator fun invoke(): List<Ingredient> =
        repo.getIngredients()
}
