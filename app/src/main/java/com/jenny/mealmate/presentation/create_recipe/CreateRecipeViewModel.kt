// File: app/src/main/java/com/jenny/mealmate/presentation/create_recipe/CreateRecipeViewModel.kt

package com.jenny.mealmate.presentation.create_recipe

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.usecases.category.GetCategoriesUseCase
import com.jenny.mealmate.domain.usecases.ingredient.GetIngredientsUseCase
import com.jenny.mealmate.domain.usecases.recipe.DeleteRecipeUseCase
import com.jenny.mealmate.domain.usecases.recipe.GetRecipeDetailUseCase
import com.jenny.mealmate.domain.usecases.recipe.SaveRecipeUseCase
import com.jenny.mealmate.util.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    savedStateHandle       : SavedStateHandle,
    private val getCats    : GetCategoriesUseCase,
    private val getIngs    : GetIngredientsUseCase,
    private val getDetail  : GetRecipeDetailUseCase,
    private val saveUseCase: SaveRecipeUseCase,
    private val deleteUseCase: DeleteRecipeUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _ui = MutableStateFlow(CreateRecipeUiState())
    val ui: StateFlow<CreateRecipeUiState> = _ui

    private val recipeId: String? = savedStateHandle.get<String>("recipeId")

    init {
        viewModelScope.launch {
            // 1) Load dropdown data
            _ui.update { it.copy(isLoading = true) }
            val cats = getCats()
            val ings = getIngs()
            _ui.update { it.copy(categories = cats, ingredientsList = ings, isLoading = false) }

            // 2) If edit mode, fetch existing recipe
            recipeId?.let { id ->
                _ui.update { it.copy(isLoading = true, isEditMode = true) }
                val res = getDetail(id)
                if (res.statusCode == ApiStatus.SUCCESS.code && res.data != null) {
                    val d = res.data
                    _ui.update { st ->
                        st.copy(
                            isLoading        = false,
                            images           = d.images.toMutableList(),
                            videoUri         = "",  // fill if your DTO includes it
                            title            = d.title,
                            selectedCategory = d.category,
                            instructions     = d.instructions,
                            ingredientRows   = d.ingredients.map {
                                IngredientRow(
                                    ingredientId = it.id,
                                    quantity     = it.quantity,
                                    unit         = it.unit
                                )
                            }
                        )
                    }
                } else {
                    _ui.update { it.copy(isLoading = false, errorMessage = res.message) }
                }
            }
        }
    }

    // Images
    fun onReplaceImages(newUris: List<String>) {
        _ui.update { it.copy(images = newUris) }
    }
    fun onRemoveImage(idx: Int) {
        _ui.update { st ->
            val updated = st.images.toMutableList().also { it.removeAt(idx) }
            st.copy(images = updated)
        }
    }
    fun onImagePicked(slot: Int, uri: String) {
        _ui.update { s ->
            val imgs = s.images.toMutableList().also { it.add(uri) }
            s.copy(images = imgs)
        }
    }

    // **New**: Video
    fun onVideoPicked(uri: String) {
        _ui.update { it.copy(videoUri = uri) }
    }

    // Simple setters
    fun onTitleChanged(v: String)        = _ui.update { it.copy(title = v) }
    fun onInstructionsChanged(v: String) = _ui.update { it.copy(instructions = v) }
    fun onCategorySelected(id: String)   = _ui.update { it.copy(selectedCategory = id) }

    // Ingredient rows
    fun onRowChanged(idx: Int, row: IngredientRow) {
        _ui.update { s ->
            val rows = s.ingredientRows.toMutableList().also { it[idx] = row }
            s.copy(ingredientRows = rows)
        }
    }
    fun onAddRow() = _ui.update {
        it.copy(ingredientRows = it.ingredientRows + IngredientRow())
    }
    fun onRemoveRow(i: Int) = _ui.update {
        it.copy(ingredientRows = it.ingredientRows.filterIndexed { idx, _ -> idx != i })
    }

    /** Called by the UI when the user taps "Create" or "Update" */
    fun onConfirm(onDone: () -> Unit) {
        viewModelScope.launch {
            val state = ui.value

            // 1) Validate
            val titleErr = state.title.takeIf { it.isBlank() }?.let { "Title is required" }
            val catErr   = state.selectedCategory.takeIf { it.isBlank() }?.let { "Category is required" }
            val insErr   = state.instructions.takeIf { it.isBlank() }?.let { "Instructions are required" }
            val photoErr = state.images.all { it.isBlank() }
                .takeIf { it }
                ?.let { "At least one photo is required" }
            val rowErrs  = state.ingredientRows.map { row ->
                row.ingredientId.isBlank() || row.quantity.isBlank() || row.unit.isBlank()
            }

            if (titleErr != null || catErr != null || insErr != null || photoErr != null || rowErrs.any { it }) {
                // surface errors
                _ui.update {
                    it.copy(
                        titleError        = titleErr,
                        categoryError     = catErr,
                        instructionsError = insErr,
                        photoError        = photoErr,
                        rowErrors         = rowErrs
                    )
                }
                return@launch
            }

            // 2) Clear previous errors & show spinner
            _ui.update {
                it.copy(
                    titleError        = null,
                    categoryError     = null,
                    instructionsError = null,
                    photoError        = null,
                    rowErrors         = List(state.ingredientRows.size) { false },
                    isSaving          = true
                )
            }

            // 3) Build request & call
            val req = SaveRecipeUseCase.Request(
                recipe_id    = recipeId.orEmpty(),
                title        = state.title,
                instructions = state.instructions,
                images       = state.images,                // already contains HTTP URLs or data-URIs
                ingredients  = state.ingredientRows.map {
                    SaveRecipeUseCase.IngDto(it.ingredientId, it.quantity, it.unit)
                },
                category     = state.selectedCategory,
                videoUrl     = state.videoUri.ifBlank { null }
            )
            val resp = saveUseCase(req)

            // 4) Done!
            if (resp.statusCode == ApiStatus.SUCCESS.code) {
                onDone()
            } else {
                _ui.update {
                    it.copy(
                        errorMessage = resp.message,
                        isSaving     = false
                    )
                }
            }
        }
    }

    fun onDelete(onDone: () -> Unit) {
        viewModelScope.launch {
            // ① show deleting spinner
            _ui.update { it.copy(isDeleting = true) }

            // ② call delete API
            val resp = deleteUseCase(recipeId.orEmpty())

            if (resp.statusCode == ApiStatus.SUCCESS.code) {
                onDone()
            } else {
                // ③ on error, hide spinner and show message
                _ui.update {
                    it.copy(
                        errorMessage = resp.message ?: "Delete failed",
                        isDeleting   = false
                    )
                }
            }
        }
    }

}
