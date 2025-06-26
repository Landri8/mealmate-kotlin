// app/src/main/java/com/jenny/mealmate/presentation/create_recipe/CategorySelector.kt
package com.jenny.mealmate.presentation.create_recipe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.domain.model.Category
import com.jenny.mealmate.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    enabled: Boolean = true,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    // Display placeholder if nothing selected
    val displayText = categories.firstOrNull { it.name == selectedCategoryId }?.name
        ?: "Select category"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = displayText,
            onValueChange = { /* readOnly */ },
            readOnly = true,
            enabled = enabled,
            placeholder = {
                Text(
                    text = "Select category",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp,
                    color = colorResource(R.color.gray)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.secondary),
                unfocusedContainerColor = colorResource(R.color.secondary),
                disabledContainerColor = colorResource(R.color.secondary),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = poppinsFamily,
                fontSize = 16.sp,
                color = if (displayText == "Select category")
                    colorResource(R.color.gray) else colorResource(R.color.black)
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { cat ->
                DropdownMenuItem(
                    text = { Text(cat.name, fontFamily = poppinsFamily) },
                    onClick = {
                        onCategorySelected(cat.name)
                        expanded = false
                    },
                    enabled = enabled
                )
            }
        }
    }

    if (error != null) {
        Text(
            text = error,
            color = Color.Red,
            fontFamily = poppinsFamily,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}
