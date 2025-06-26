package com.jenny.mealmate.domain.repository

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.domain.model.HomeData

interface HomeRepository {
    suspend fun fetchHomeData(userId: String): ApiResponseDto<HomeData>
}