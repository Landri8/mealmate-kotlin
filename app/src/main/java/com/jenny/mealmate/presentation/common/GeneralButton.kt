package com.jenny.mealmate.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun GeneralButton (
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.primary),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 9.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 48.dp)
    ) {
        Text(
            text= text,
            fontSize = 16.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun GeneralButtonPreview () {
    MealmateTheme {
        GeneralButton(modifier = Modifier.fillMaxWidth(), text = "Get Started", onClick = { println("HELLO") })
    }
}