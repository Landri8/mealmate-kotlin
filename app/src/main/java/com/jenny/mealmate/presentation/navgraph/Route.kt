package com.jenny.mealmate.presentation.navgraph

sealed class Route (
    val route: String
){
    object OnBoardingScreen: Route(route = "onBoardingScreen")
    object HomeScreen: Route(route = "homeScreen")
    object LoginScreen: Route(route = "loginScreen")
    object RegisterScreen: Route(route = "registerScreen")
    object RecipeDetailScreen: Route(route = "recipeDetailScreen")
    object CreateRecipeScreen: Route(route = "createRecipeScreen")

    object AppStartNavigation: Route(route = "appStartNavigation")
    object AuthNavigation: Route(route = "authNavigation")
    object AppNavigation: Route(route = "appNavigation")
}