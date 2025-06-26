package com.jenny.mealmate.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.jenny.mealmate.domain.model.Ingredient

data class IngredientDto(
    @SerializedName("_id")       val _id: String,
    @SerializedName("name")      val name: String,
    @SerializedName("image")     val image: String,
    @SerializedName("id")        val id: String,
    @SerializedName("createdAt") val createdAt: String
) {
    fun toDomain() = Ingredient(
        id        = id,
        name      = name
    )
}
