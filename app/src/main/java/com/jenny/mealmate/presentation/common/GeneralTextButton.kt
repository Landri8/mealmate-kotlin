package com.jenny.mealmate.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.ui.theme.MealmateTheme
import com.jenny.mealmate.ui.theme.poppinsFamily

@Composable
fun GeneralTextButton (
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
    ) {
        Text(
            text= text,
            fontSize = 16.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.SemiBold,
            color= Color.Gray
        )
    }
}


@Preview
@Composable
fun GeneralTextButtonPreview () {
    MealmateTheme {
        GeneralTextButton(text = "Get Started", onClick = { println("HELLO") })
    }
}