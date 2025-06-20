package com.jenny.mealmate.di

import android.app.Application
import com.jenny.mealmate.data.manager.LocalUserManagerImpl
import com.jenny.mealmate.data.remote.api.AuthApi
import com.jenny.mealmate.data.repository.AuthRepositoryImpl
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.repository.AuthRepository
import com.jenny.mealmate.domain.usecases.AppEntryUseCases
import com.jenny.mealmate.domain.usecases.ReadAppEntry
import com.jenny.mealmate.domain.usecases.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl("https://9cdd-2001-fb1-bc-2d71-71df-f42b-2cdf-205.ngrok-free.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    fun provideAuthRepository(api: AuthApi): AuthRepository =
        AuthRepositoryImpl(api)

}