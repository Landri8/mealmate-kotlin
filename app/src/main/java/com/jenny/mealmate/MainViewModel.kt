package com.jenny.mealmate

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
        appEntryUseCases.readAppEntry().onEach { hasSeenOnboarding ->
            if (!hasSeenOnboarding) {
                startDestination = Route.AppStartNavigation.route
            } else {
                localUserManager.readIsUserLoggedIn().onEach { isLoggedIn ->
                    startDestination = if (isLoggedIn) {
                        Route.HomeScreen.route
                    } else {
                        Route.AuthNavigation.route
                    }
                    delay(300)
                    splashCondition = false
                }.launchIn(viewModelScope)
            }
        }.launchIn(viewModelScope)
    }
}