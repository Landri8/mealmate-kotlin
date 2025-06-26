package com.jenny.mealmate.presentation.recipe_detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.manager.BookmarkManager
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.model.RecipeDetail
import com.jenny.mealmate.domain.usecases.recipe.GetRecipeDetailUseCase
import com.jenny.mealmate.util.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getDetail: GetRecipeDetailUseCase,
    private val bookmarkManager: BookmarkManager,
    private val localUserManager: LocalUserManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState

    private val recipeId: String =
        checkNotNull(savedStateHandle.get<String>("recipeId"))

    init {
        viewModelScope.launch {
            val user = localUserManager.readUser().first()
            _uiState.update { it.copy(currentUserId = user.id) }
        }
        fetchDetail()
        observeBookmark()
    }

    fun refresh() = fetchDetail()

    private fun fetchDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getDetail(recipeId)
            if (result.statusCode == ApiStatus.SUCCESS.code && result.data != null) {
                val detail = result.data
                _uiState.update { it.copy(isLoading = false, detail = detail) }

                // ← If it’s bookmarked, keep that bookmark up-to-date:
                if (_uiState.value.isBookmarked) {
                    bookmarkManager.updateBookmark(detail)
                }
            } else {
                val msg = result.message ?: "Failed to load"
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
            }
        }
    }

    private fun observeBookmark() {
        viewModelScope.launch {
            bookmarkManager.isBookmarked(recipeId)
                .collect { bookmarked ->
                    _uiState.update { it.copy(isBookmarked = bookmarked) }
                }
        }
    }

    /** Called from UI when bookmark icon is tapped */
    fun onToggleBookmark(detail: RecipeDetail) {
        viewModelScope.launch {
            if (bookmarkManager.isBookmarked(detail.id).first()) {
                // going _off_
                bookmarkManager.toggleBookmark(detail.id)
            } else {
                // going _on_ — first persist the JSON, then flip the flag
                bookmarkManager.updateBookmark(detail)
                bookmarkManager.toggleBookmark(detail.id)
            }
        }
    }
}
