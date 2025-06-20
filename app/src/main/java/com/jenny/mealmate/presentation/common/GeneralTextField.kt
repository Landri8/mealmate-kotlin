package com.jenny.mealmate.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.ui.theme.poppinsFamily

@Composable
fun GeneralTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    isPassword: Boolean = false,
    shape: Shape = RoundedCornerShape(9.dp),
    containerColor: Color = colorResource(id = R.color.secondary),
    indicatorColor: Color = Color.Transparent,
    fontFamily: FontFamily = poppinsFamily,
    error: String? = null // ✅ NEW
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = label, fontFamily = poppinsFamily, color = colorResource(R.color.gray)) },
            shape = shape,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    val image = if (passwordVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                errorContainerColor = containerColor,
                focusedIndicatorColor = indicatorColor,
                unfocusedIndicatorColor = indicatorColor,
                errorIndicatorColor = indicatorColor
            ),
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontSize = fontSize,
                color = colorResource(R.color.black),
            ),
            isError = error != null, // ✅ Highlights border (if used)
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                color = colorResource(R.color.errorColor),
                fontFamily = poppinsFamily,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}


@Preview
@Composable
fun GeneralTextFieldPreview () {
    GeneralTextField(
        value = "",
        onValueChange = {  },
        label = "Username"
    )
}