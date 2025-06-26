package com.jenny.mealmate.presentation.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenny.mealmate.domain.manager.LocalUserManager
import com.jenny.mealmate.domain.usecases.auth.LoginUserUseCase
import com.jenny.mealmate.domain.usecases.auth.RegisterUserUseCase
import com.jenny.mealmate.presentation.common.toaster.Toaster
import com.jenny.mealmate.util.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUserUseCase,
    private val loginUseCase: LoginUserUseCase,
    private val localUserManager: LocalUserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(value: String) = updateState { copy(username = value) }
    fun onEmailChanged(value: String) = updateState { copy(email = value) }
    fun onPasswordChanged(value: String) = updateState { copy(password = value) }
    fun onConfirmPasswordChanged(value: String) = updateState { copy(confirmPassword = value) }

    private fun updateState(update: AuthUiState.() -> AuthUiState) {
        _uiState.update(update)
    }

    fun registerUser(onSuccess: () -> Unit) {
        val state = _uiState.value
        var hasError = false

        _uiState.update {
            it.copy(
                usernameError = if (state.username.isBlank()) {
                    hasError = true
                    "Username cannot be empty"
                } else null,
                emailError = when {
                    state.email.isBlank() -> {
                        hasError = true
                        "Email cannot be empty"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(state.email).matches() -> {
                        hasError = true
                        "Invalid email format"
                    }
                    else -> null
                },
                passwordError = if (state.password.length < 6) {
                    hasError = true
                    "Password must be at least 6 characters"
                } else null,
                confirmPasswordError = if (state.password != state.confirmPassword) {
                    hasError = true
                    "Passwords do not match"
                } else null
            )
        }

        if (hasError) return

        viewModelScope.launch {
            try {
                val result = registerUseCase(state.username, state.email, state.password, state.confirmPassword)

                if (result.statusCode == ApiStatus.FAIL.code || result.statusCode == ApiStatus.ERROR.code) {
//                  clearForm()
                    Toaster.show(result.message)
                } else if (result.data != null) {
                    val responseData = result.data
                    localUserManager.saveUserAuth(
                        id = responseData.user.id,
                        name =  responseData.user.name,
                        email = responseData.user.email,
                        token = responseData.token,
                        createdAt = responseData.user.createdAt
                    )
                    onSuccess()
                }
            } catch (e: Exception) {
                Toaster.show("Something went wrong!")
            }
        }
    }

    fun loginUser(onSuccess: () -> Unit) {
        val state = _uiState.value
        var hasError = false

        _uiState.update {
            it.copy(
                emailError = when {
                    state.email.isBlank() -> {
                        hasError = true
                        "Email cannot be empty"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(state.email).matches() -> {
                        hasError = true
                        "Invalid email format"
                    }
                    else -> null
                },
                passwordError = if (state.password.isBlank()) {
                    hasError = true
                    "Password cannot be empty"
                } else null
            )
        }

        if (hasError) return

        viewModelScope.launch {
            try {
                val result = loginUseCase(state.email, state.password)

                if (result.statusCode == ApiStatus.FAIL.code || result.statusCode == ApiStatus.ERROR.code) {
                    clearForm()
                    Toaster.show(result.message)
                } else if (result.data != null) {
                    val responseData = result.data
                    localUserManager.saveUserAuth(
                        id = responseData.user.id,
                        name =  responseData.user.name,
                        email = responseData.user.email,
                        token = responseData.token,
                        createdAt = responseData.user.createdAt
                    )

                    onSuccess()
                }
            } catch (e: Exception) {
                Toaster.show("Something went wrong!")
            }
        }
    }

    private fun clearForm() {
        _uiState.update {
            it.copy(
                username = "",
                email = "",
                password = "",
                confirmPassword = "",
                usernameError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null
            )
        }
    }
}
