package com.jenny.mealmate.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.domain.model.Recipe
import com.jenny.mealmate.ui.theme.poppinsFamily

@Composable
fun HorizontalSection(
    title: String,
    items: List<Recipe>,
    modifier: Modifier = Modifier,
    onItemClick: (Recipe) -> Unit = {}
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFamily,
                fontSize = 22.sp,
                color = colorResource(id = R.color.black)
            )
            Text(
                text = "See all",
                fontFamily = poppinsFamily,
                fontSize = 16.sp,
                color = colorResource(id = R.color.primary),
                modifier = Modifier.clickable(onClick = {})
            )
        }

        Spacer(Modifier.height(20.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    modifier = Modifier.width(160.dp),
                    onClick = { onItemClick(recipe) }
                )
            }
        }
    }
}
