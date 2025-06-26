package com.jenny.mealmate.presentation.create_recipe

import com.jenny.mealmate.domain.model.Category
import com.jenny.mealmate.domain.model.Ingredient

data class CreateRecipeUiState(
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,

    val images: List<String> = emptyList(),
    val videoUri: String = "",

    val title: String = "",
    val titleError: String? = null,

    val categories: List<Category> = emptyList(),
    val selectedCategory: String = "",
    val categoryError: String? = null,

    val instructions: String = "",
    val instructionsError: String? = null,

    val ingredientsList: List<Ingredient> = emptyList(),
    val ingredientRows: List<IngredientRow> = listOf(IngredientRow()),
    val rowErrors: List<Boolean> = emptyList(),

    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val photoError:    String? = null,
)

data class IngredientRow(
    val ingredientId: String = "",
    val quantity    : String = "",
    val unit        : String = ""
)
