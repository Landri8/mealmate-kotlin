package com.jenny.mealmate.presentation.onboarding

import androidx.annotation.DrawableRes
import com.jenny.mealmate.R

data class Page(
    val title: String,
    val desc: String,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Struggling to Decide What to Eat?",
        desc = "Too many options, too little time? MealMate helps you break the cycle of indecision with organized, ready-to-go weekly plans.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Plan & Create with Confidence",
        desc = "From custom recipes to smart grocery lists, take full control of your meals. Save time, cook with joy, and eat better every day.",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Enjoy a Full, Balanced Week",
        desc = "Get everything you need in one place—meals, groceries, and reminders. MealMate makes it easy to stick to your goals and enjoy every bite.",
        image = R.drawable.onboarding3
    )
)