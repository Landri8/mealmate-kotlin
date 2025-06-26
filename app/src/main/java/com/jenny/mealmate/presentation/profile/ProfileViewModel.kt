package com.jenny.mealmate.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val localUserManager: LocalUserManager
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            // keep observing stored user
            localUserManager.readUser()
                .onEach { _user.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            localUserManager.clearUserAuth()
            onLoggedOut()
        }
    }
}
