package com.jenny.mealmate.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.jenny.mealmate.presentation.auth.AuthViewModel
import com.jenny.mealmate.presentation.auth.LoginScreen
import com.jenny.mealmate.presentation.auth.RegisterScreen
import com.jenny.mealmate.presentation.onboarding.OnBoardingScreen
import com.jenny.mealmate.presentation.onboarding.OnBoardingViewModel

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
                        navController.navigate(Route.RegisterScreen.route) {
                            popUpTo(Route.LoginScreen.route) { inclusive = true }
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
                            popUpTo(Route.RegisterScreen.route) { inclusive = true }
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
            composable(route = Route.HomeScreen.route) {
                Text(text = "Min ga lar par")
            }

        }

    }
}