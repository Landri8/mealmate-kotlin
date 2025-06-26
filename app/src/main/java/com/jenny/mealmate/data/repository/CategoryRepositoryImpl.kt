package com.jenny.mealmate.data.repository

import com.jenny.mealmate.data.remote.api.CategoryApi
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.model.Category
import com.jenny.mealmate.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: CategoryApi,
    private val localUserManager: LocalUserManager
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        val token = localUserManager.readUser().first().token
        val bearer = "Bearer $token"

        return api.list(bearer).data
            .orEmpty()
            .map { it.toDomain() }
    }

}
