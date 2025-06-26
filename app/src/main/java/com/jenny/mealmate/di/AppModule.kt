// app/src/main/java/com/jenny/mealmate/di/AppModule.kt
package com.jenny.mealmate.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.jenny.mealmate.data.manager.BookmarkManagerImpl
import com.jenny.mealmate.data.manager.LocalUserManagerImpl
import com.jenny.mealmate.data.remote.api.AuthApi
import com.jenny.mealmate.data.remote.api.CategoryApi
import com.jenny.mealmate.data.remote.api.HomeApi
import com.jenny.mealmate.data.remote.api.IngredientApi
import com.jenny.mealmate.data.remote.api.RecipeApi
import com.jenny.mealmate.data.repository.AuthRepositoryImpl
import com.jenny.mealmate.data.repository.CategoryRepositoryImpl
import com.jenny.mealmate.data.repository.HomeRepositoryImpl
import com.jenny.mealmate.data.repository.IngredientRepositoryImpl
import com.jenny.mealmate.data.repository.RecipeRepositoryImpl
import com.jenny.mealmate.domain.manager.BookmarkManager
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.repository.AuthRepository
import com.jenny.mealmate.domain.repository.CategoryRepository
import com.jenny.mealmate.domain.repository.HomeRepository
import com.jenny.mealmate.domain.repository.IngredientRepository
import com.jenny.mealmate.domain.repository.RecipeRepository
import com.jenny.mealmate.domain.usecases.AppEntryUseCases
import com.jenny.mealmate.domain.usecases.ReadAppEntry
import com.jenny.mealmate.domain.usecases.SaveAppEntry
import com.jenny.mealmate.domain.usecases.category.GetCategoriesUseCase
import com.jenny.mealmate.domain.usecases.home.GetHomeDataUseCase
import com.jenny.mealmate.domain.usecases.ingredient.GetIngredientsUseCase
import com.jenny.mealmate.domain.usecases.recipe.DeleteRecipeUseCase
import com.jenny.mealmate.domain.usecases.recipe.GetRecipeDetailUseCase
import com.jenny.mealmate.domain.usecases.recipe.SaveRecipeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Retrofit instance ----
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // 1) Create the logging interceptor
        val logging = HttpLoggingInterceptor().apply {
            // set to BODY for full request+response bodies
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2) Add it to your OkHttp client
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // 3) Build Retrofit with that client
        return Retrofit.Builder()
            .baseUrl("https://9f94-2a09-bac5-56bf-101e-00-19b-18f.ngrok-free.app/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- Auth feature ----
    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideAuthRepository(api: AuthApi): AuthRepository =
        AuthRepositoryImpl(api)

    // --- Home feature ----
    @Provides @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)

    @Provides @Singleton
    fun provideHomeRepository(
        api: HomeApi,
        localUserManager: LocalUserManager
    ): HomeRepository = HomeRepositoryImpl(api, localUserManager)

    @Provides @Singleton
    fun provideGetHomeDataUseCase(repo: HomeRepository): GetHomeDataUseCase =
        GetHomeDataUseCase(repo)

    // --- Categories feature ----
    @Provides @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides @Singleton
    fun provideCategoryRepository(api: CategoryApi, localUserManager: LocalUserManager): CategoryRepository =
        CategoryRepositoryImpl(api, localUserManager)

    @Provides @Singleton
    fun provideGetCategoriesUseCase(repo: CategoryRepository): GetCategoriesUseCase =
        GetCategoriesUseCase(repo)

    // --- Ingredients feature ----
    @Provides @Singleton
    fun provideIngredientApi(retrofit: Retrofit): IngredientApi =
        retrofit.create(IngredientApi::class.java)

    @Provides @Singleton
    fun provideIngredientRepository(api: IngredientApi, localUserManager: LocalUserManager): IngredientRepository =
        IngredientRepositoryImpl(api, localUserManager)

    @Provides @Singleton
    fun provideGetIngredientsUseCase(repo: IngredientRepository): GetIngredientsUseCase =
        GetIngredientsUseCase(repo)

    // --- Recipes feature ----
    @Provides @Singleton
    fun provideRecipeApi(retrofit: Retrofit): RecipeApi =
        retrofit.create(RecipeApi::class.java)

    @Provides @Singleton
    fun provideRecipeRepository(repoImpl: RecipeRepositoryImpl): RecipeRepository =
        repoImpl

    @Provides @Singleton
    fun provideGetRecipeDetailUseCase(repo: RecipeRepository): GetRecipeDetailUseCase =
        GetRecipeDetailUseCase(repo)

    @Provides @Singleton
    fun provideSaveRecipeUseCase(repo: RecipeRepository): SaveRecipeUseCase =
        SaveRecipeUseCase(repo)

    @Provides @Singleton
    fun provideDeleteRecipeUseCase(repo: RecipeRepository): DeleteRecipeUseCase =
        DeleteRecipeUseCase(repo)

    // --- Local managers & Onboarding ----
    @Provides @Singleton
    fun provideLocalUserManager(app: Application): LocalUserManager =
        LocalUserManagerImpl(app)

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("user_prefs") }
        )
    }

    @Provides @Singleton
    fun provideBookmarkManager(app: Application, pref: DataStore<Preferences>): BookmarkManager =
        BookmarkManagerImpl(app, pref)

    @Provides @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )
}
