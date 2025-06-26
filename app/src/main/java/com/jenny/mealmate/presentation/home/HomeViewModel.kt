package com.jenny.mealmate.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.usecases.home.GetHomeDataUseCase
import com.jenny.mealmate.presentation.common.toaster.Toaster
import com.jenny.mealmate.util.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: GetHomeDataUseCase,
    private val localUserManager: LocalUserManager
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewModelScope.launch {
            // 1) Start loading
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            // 2) Load user and first name
            val user = localUserManager.readUser().first()
            val firstName = user.name.split(" ").firstOrNull().orEmpty()
            _state.update { it.copy(firstName = firstName) }

            // 3) Call API
            val result = homeUseCase(user.id)

            // 4) Handle response
            if (result.statusCode == ApiStatus.SUCCESS.code && result.data != null) {
                _state.update {
                    it.copy(
                        recipes = result.data.userCreatedRecipes,
                        recommendations = result.data.recommendations,
                        categories = result.data.categories,
                        selectedCategory = result.data.categories.firstOrNull()?.categoryId.orEmpty(),
                        isLoading = false
                    )
                }
            } else {
                val message = result.message ?: "Something went wrong!"
                // show toast
                Toaster.show(message)
                // update error state
                _state.update {
                    it.copy(isLoading = false, errorMessage = message)
                }
            }
        }
    }

    fun refresh() {
        fetchHomeData()
    }

    fun onCategorySelected(categoryId: String) {
        _state.update { it.copy(selectedCategory = categoryId) }
    }
}
