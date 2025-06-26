package com.jenny.mealmate.data.remote.api

import com.jenny.mealmate.data.remote.dto.ApiResponseDto
import com.jenny.mealmate.data.remote.dto.HomeDataDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HomeApi {
    @POST("app")
    suspend fun getHomeData(
        @Header("Authorization") auth: String,
        @Body body: Map<String, String>
    ): ApiResponseDto<HomeDataDto>
}
