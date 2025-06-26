// app/src/main/java/com/jenny/mealmate/presentation/bookmark/BookmarkViewModel.kt
package com.jenny.mealmate.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.manager.BookmarkManager
import com.jenny.mealmate.domain.model.RecipeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkManager: BookmarkManager
) : ViewModel() {

    private val _bookmarks = MutableStateFlow<List<RecipeDetail>>(emptyList())
    val bookmarks: StateFlow<List<RecipeDetail>> = _bookmarks

    init {
        viewModelScope.launch {
            // assume getAll() returns Flow<List<RecipeDetail>>
            bookmarkManager.getAllBookmarks().collect { list ->
                _bookmarks.value = list
            }
        }
    }
}
