package com.jenny.mealmate.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.jenny.mealmate.R
import com.jenny.mealmate.presentation.auth.AuthViewModel
import com.jenny.mealmate.presentation.auth.LoginScreen
import com.jenny.mealmate.presentation.auth.RegisterScreen
import com.jenny.mealmate.presentation.bookmark.BookmarkScreen
import com.jenny.mealmate.presentation.bookmark.BookmarkViewModel
import com.jenny.mealmate.presentation.create_recipe.CreateOrEditRecipeScreen
import com.jenny.mealmate.presentation.create_recipe.CreateRecipeViewModel
import com.jenny.mealmate.presentation.home.HomeScreen
import com.jenny.mealmate.presentation.home.HomeViewModel
import com.jenny.mealmate.presentation.onboarding.OnBoardingScreen
import com.jenny.mealmate.presentation.onboarding.OnBoardingViewModel
import com.jenny.mealmate.presentation.profile.ProfileScreen
import com.jenny.mealmate.presentation.profile.ProfileViewModel
import com.jenny.mealmate.presentation.recipe_detail.RecipeDetailScreen
import com.jenny.mealmate.presentation.recipe_detail.RecipeDetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(event = viewModel::onEvent)
            }
        }

        navigation(
            route = Route.AuthNavigation.route,
            startDestination = Route.RegisterScreen.route
        ) {
            composable(route = Route.LoginScreen.route) {
                val authViewModel: AuthViewModel = hiltViewModel()
                LoginScreen (
                    onNavigateToHome = {
                        navController.navigate(Route.HomeScreen.route) {
                            popUpTo(Route.RegisterScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Route.HomeScreen.route) {
                            popUpTo(Route.AuthNavigation.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Register Screen
            composable(route = Route.RegisterScreen.route) {
                val authViewModel: AuthViewModel = hiltViewModel()
                RegisterScreen(
                    onNavigateToHome = {
                        navController.navigate(Route.HomeScreen.route) {
                            popUpTo(Route.AuthNavigation.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Route.LoginScreen.route) {
                            popUpTo(Route.RegisterScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        navigation(
            route = Route.AppNavigation.route,
            startDestination = Route.HomeScreen.route
        ) {
            // Home
            composable(Route.HomeScreen.route) {
                val homeVm: HomeViewModel = hiltViewModel()
                val state by homeVm.state.collectAsState()

                HomeScreen(
                    firstName          = state.firstName,
                    recipes            = state.recipes,
                    recommendations    = state.recommendations,
                    categories         = state.categories,
                    selectedCategory   = state.selectedCategory,
                    onCategorySelected = homeVm::onCategorySelected,
                    onSearchClick      = { /* … */ },
                    onRecipeClick      = { recipe ->
                        navController.navigate("${Route.RecipeDetailScreen.route}/${recipe.id}")
                    },
                    onCreateClick      = {
                        navController.navigate(Route.CreateOrEditRecipe.route)
                    },
                    onBookmarkClick    = {
                        navController.navigate(Route.BookmarkScreen.route)
                    },
                    onRefresh        = { homeVm.refresh() },
                    onProfileClick = {
                        navController.navigate(Route.ProfileScreen.route)
                    },
                    isLoading          = state.isLoading,
                    errorMessage       = state.errorMessage
                )
            }

            // Detail
            composable(
                route = "${Route.RecipeDetailScreen.route}/{recipeId}",
                arguments = listOf(navArgument("recipeId"){ type = NavType.StringType })
            ) { backStack ->

                val vm: RecipeDetailViewModel = hiltViewModel(backStack)
                // 1) Observe your ViewModel state
                val ui by vm.uiState.collectAsState()

                // 2) Observe your “refresh” flag
                val shouldRefresh by backStack
                    .savedStateHandle
                    .getStateFlow("refresh", false)
                    .collectAsState()

                // 3) When it flips true, pull again and clear it
                LaunchedEffect(shouldRefresh) {
                    if (shouldRefresh) {
                        vm.refresh()
                        backStack.savedStateHandle["refresh"] = false
                    }
                }

                // 4) Now render based on loading / error / data
                when {
                    ui.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colorResource(R.color.primary))
                        }
                    }
                    ui.errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = ui.errorMessage!!, color = Color.Red)
                        }
                    }
                    ui.detail != null -> {
                        RecipeDetailScreen(
                            viewModel = vm,
                            onBack   = { navController.popBackStack() },
                            onShare  = { },
                            onEdit   = {
                                navController.navigate("${Route.CreateOrEditRecipe.route}/${ui.detail!!.id}")
                            }
                        )
                    }
                }
            }

            // Create (no ID)
            composable(Route.CreateOrEditRecipe.route) {
                val vm: CreateRecipeViewModel = hiltViewModel()
                CreateOrEditRecipeScreen(
                    viewModel  = vm,
                    recipeId   = null,
                    onBack     = { navController.popBackStack() },
                    onFinished = { navController.popBackStack() },
                    onDeleted   = {
                        // after delete, pop twice so you're back on Home
                        navController.popBackStack()      // pop edit screen
                        navController.popBackStack()      // pop detail screen
                    }
                )
            }

            // Edit (with ID)
            composable(
                route = "${Route.CreateOrEditRecipe.route}/{recipeId}",
                arguments = listOf(navArgument("recipeId"){ type = NavType.StringType })
            ) { backStack ->
                val vm: CreateRecipeViewModel = hiltViewModel(backStack)
                val recipeId = backStack.arguments!!.getString("recipeId")
                CreateOrEditRecipeScreen(
                    viewModel  = vm,
                    recipeId   = recipeId,
                    onBack     = { navController.popBackStack() },
                    onFinished = {
                        // <-- mark the detail screen to refresh
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refresh", true)
                        navController.popBackStack()
                    },
                    onDeleted   = {
                        // after delete, pop twice so you're back on Home
                        navController.popBackStack()      // pop edit screen
                        navController.popBackStack()      // pop detail screen
                    }
                )
            }

            composable(Route.BookmarkScreen.route) {
                val vm: BookmarkViewModel = hiltViewModel()
                BookmarkScreen(
                    viewModel      = vm,
                    onBack         = { navController.popBackStack() },
                    onRecipeClick  = { id ->
                        navController.navigate("${Route.RecipeDetailScreen.route}/$id")
                    }
                )
            }

            composable(Route.ProfileScreen.route) {
                // grab the Hilt-provided ViewModel
                val profileVm: ProfileViewModel = hiltViewModel()

                ProfileScreen(
                    viewModel = profileVm,
                    onLogout = {
                        // clear back-stack and go to login
                        navController.navigate(Route.RegisterScreen.route) {
                            popUpTo(Route.AppNavigation.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }
        }

    }
}