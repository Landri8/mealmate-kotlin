package com.jenny.mealmate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.usecases.AppEntryUseCases
import com.jenny.mealmate.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases,
    private val localUserManager: LocalUserManager
) : ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        combine(
            appEntryUseCases.readAppEntry(),
            localUserManager.readIsUserLoggedIn()
        ) { hasSeenOnboarding: Boolean, isLoggedIn: Boolean ->
            when {
                !hasSeenOnboarding -> Route.AppStartNavigation.route
                isLoggedIn -> Route.AppNavigation.route
                else -> Route.AuthNavigation.route
            }.also {
                Log.d("APP_START", "OnboardingSeen=$hasSeenOnboarding, isLoggedIn=$isLoggedIn, destination=$it")
            }
        }.onEach { destination ->
            startDestination = destination
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)
    }
}