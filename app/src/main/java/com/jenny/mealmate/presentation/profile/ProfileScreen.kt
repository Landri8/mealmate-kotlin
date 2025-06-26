package com.jenny.mealmate.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onBack:() -> Unit
) {
    val user by viewModel.user.collectAsState()

    Scaffold(
        containerColor = colorResource(R.color.bgColor),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (user == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // typical AppBar height
                    contentAlignment = Alignment.Center
                ) {
                    // Centered Title
                    Text(
                        text = "Profile",
                        fontFamily = poppinsFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.black)
                    )

                    // Left and Right actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = colorResource(R.color.black),
                                modifier = Modifier.size(38.dp)
                            )
                        }


                    }
                }

                Spacer(Modifier.size(4.dp))

                InfoRow("Name",     user!!.name)
                Spacer(Modifier.size(0.5.dp))
                InfoRow("Email",    user!!.email)
                Spacer(Modifier.size(0.5.dp))
                InfoRow("User ID",  user!!.id)
                Spacer(Modifier.size(0.5.dp))
                InfoRow("Joined",   user!!.createdAt)
                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { viewModel.logout(onLogout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC6C6),
                        contentColor = Color.Red,
                        disabledContentColor = Color.White,
                        disabledContainerColor = colorResource(R.color.primary),
                    )
                ) {
                    Text(
                        text = "Log out",
                        fontFamily = poppinsFamily,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label,     fontFamily = poppinsFamily, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 2.dp))
        Text(value,     fontFamily = poppinsFamily, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
    }
}
