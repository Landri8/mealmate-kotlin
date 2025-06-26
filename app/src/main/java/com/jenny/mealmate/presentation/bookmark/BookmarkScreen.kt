// app/src/main/java/com/jenny/mealmate/presentation/bookmark/BookmarkScreen.kt
package com.jenny.mealmate.presentation.bookmark

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jenny.mealmate.R
import com.jenny.mealmate.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel,
    onBack:        () -> Unit,
    onRecipeClick: (String) -> Unit
) {
    val list by viewModel.bookmarks.collectAsState()

    Scaffold(containerColor = colorResource(R.color.bgColor)) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Header ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // typical AppBar height
                contentAlignment = Alignment.Center
            ) {
                // Centered Title
                Text(
                    text = "Bookmarks",
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

            Spacer(Modifier.height(8.dp))

            // ── Content ────────────────────────
            if (list.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = colorResource(R.color.primary))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(list) { recipe ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable { onRecipeClick(recipe.id) },
                            colors = CardColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent,
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model            = recipe.images.firstOrNull(),
                                    contentDescription = null,
                                    modifier         = Modifier
                                        .fillMaxWidth(0.3f)
                                        .height(100.dp),
                                    contentScale     = ContentScale.Crop
                                )
                                Column(
                                    Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text       = recipe.title,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize   = 18.sp,
                                        color = colorResource(R.color.black)
                                    )
                                    Text(
                                        text       = "By ${recipe.createdByName}",
                                        fontFamily = poppinsFamily,
                                        fontSize   = 14.sp,
                                        color      = colorResource(R.color.darkGray)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
