package com.jenny.mealmate.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()

    fun readAppEntry() : Flow<Boolean>

    suspend fun saveUserAuth(id: String, name: String, email: String, token: String, createdAt: String)
    fun readIsUserLoggedIn(): Flow<Boolean>
    suspend fun clearUserAuth()
}