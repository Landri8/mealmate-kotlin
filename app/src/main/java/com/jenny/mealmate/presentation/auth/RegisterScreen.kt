package com.jenny.mealmate.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jenny.mealmate.R
import com.jenny.mealmate.presentation.common.GeneralButton
import com.jenny.mealmate.presentation.common.GeneralTextField
import com.jenny.mealmate.ui.theme.poppinsFamily

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val title : String = "Register a new account\nand start your journey today"
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.fillMaxHeight(0.12f))

        Image(
            modifier = Modifier.size(66.dp),
            painter = painterResource(id = R.drawable.inapplogo),
            contentDescription = null
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = title,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFamily,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            color = colorResource(id = R.color.black)
        )

        Spacer(Modifier.height(32.dp))

        GeneralTextField(
            value = state.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            label = "Username",
            error = state.usernameError
        )

        Spacer(Modifier.height(12.dp))

        GeneralTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = "Email",
            error = state.emailError
        )

        Spacer(Modifier.height(12.dp))

        GeneralTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = "Password",
            isPassword = true,
            error = state.passwordError
        )

        Spacer(Modifier.height(12.dp))

        GeneralTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            label = "Confirm Password",
            isPassword = true,
            error = state.confirmPasswordError
        )


        Spacer(Modifier.height(36.dp))

        GeneralButton (
            modifier = Modifier.fillMaxWidth(),
            text = "Sign Up",
            onClick = {
                viewModel.registerUser {
                    onNavigateToHome()
                }
            },
        )

        Spacer(Modifier.height(38.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){

            Text("Already have an account?", fontFamily = poppinsFamily, fontSize = 16.sp)
            TextButton (onClick = onNavigateToLogin) {
                Text("Log in", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = poppinsFamily, color = colorResource(R.color.primary))
            }
        }
    }
}
