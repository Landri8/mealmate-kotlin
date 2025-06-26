package com.jenny.mealmate.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.jenny.mealmate.domain.model.Category

data class CategoryInfoDto(
    @SerializedName("_id")        val _id: String,
    @SerializedName("title")      val title: String,
    @SerializedName("description")val description: String,
    @SerializedName("id")         val id: String,
    @SerializedName("createdAt")  val createdAt: String
) {
    fun toDomain() = Category(
        id          = id,
        name        = title,
    )
}
