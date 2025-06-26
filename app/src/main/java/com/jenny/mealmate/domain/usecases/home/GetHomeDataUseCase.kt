package com.jenny.mealmate.domain.usecases.home

import com.jenny.mealmate.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val repo: HomeRepository
) {
    suspend operator fun invoke(userId: String) = repo.fetchHomeData(userId)
}
