package com.jenny.mealmate.util

enum class ApiStatus(val code: Int) {
    SUCCESS(200),
    FAIL(400),
    ERROR(500);

    companion object {
        fun fromCode(code: Int): ApiStatus? = values().find { it.code == code }
    }
}