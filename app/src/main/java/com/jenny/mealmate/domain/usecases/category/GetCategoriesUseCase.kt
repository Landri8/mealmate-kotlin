package com.jenny.mealmate.domain.usecases.category

import com.jenny.mealmate.domain.model.Category
import com.jenny.mealmate.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repo: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> =
        repo.getCategories()
}
