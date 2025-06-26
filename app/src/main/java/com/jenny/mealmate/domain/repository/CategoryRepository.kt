package com.jenny.mealmate.domain.repository

import com.jenny.mealmate.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
