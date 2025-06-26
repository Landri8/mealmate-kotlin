package com.jenny.mealmate.presentation.recipe_detail

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jenny.mealmate.R
import com.jenny.mealmate.domain.model.RecipeDetail
import com.jenny.mealmate.ui.theme.poppinsFamily

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecipeDetailScreen(
    onBack: () -> Unit,
    onShare: (RecipeDetail) -> Unit = {},
    onEdit: (RecipeDetail) -> Unit  = {},
    viewModel: RecipeDetailViewModel
) {
    val ui by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = colorResource(R.color.bgColor),
//        floatingActionButton = {
//            if (!ui.isLoading) {
//                FloatingActionButton(
//                    onClick = { /* TODO: play tutorial */ },
//                    modifier = Modifier
//                        .padding(horizontal = 18.dp),
//                    containerColor = colorResource(R.color.primary),
//                    contentColor = colorResource(R.color.white),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Row(
//                        Modifier.padding(horizontal = 16.dp),
//                        horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PlayCircle,
//                            contentDescription = "Watch Tutorial"
//                        )
//                        Text(
//                            text = "Watch Tutorial",
//                            fontFamily = poppinsFamily,
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//            }
//        },
//        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                ui.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorResource(R.color.primary)
                    )
                }
                ui.errorMessage != null -> {
                    Text(
                        text = ui.errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center),
                        fontFamily = poppinsFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                ui.detail != null -> {
                    val d = ui.detail!!
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Back button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), // Ensure same height as title AppBar
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Back button
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        modifier = Modifier.size(38.dp),
                                        tint = colorResource(R.color.black)
                                    )
                                }

                                // Right-side actions (Share and Edit)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically // changed to CenterVertically for visual balance
                                ) {
                                    // Share button
                                    IconButton(
                                        onClick = {
                                            val shareText = buildString {
                                                appendLine("🍽️ Recipe: ${d.title}")
                                                appendLine()
                                                appendLine("🛒 Ingredients:")
                                                d.ingredients.forEach { ing ->
                                                    appendLine("• ${ing.name} — ${ing.quantity} ${ing.unit}")
                                                }
                                            }

                                            Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_SUBJECT, d.title)
                                                putExtra(Intent.EXTRA_TEXT, shareText)
                                            }.also { shareIntent ->
                                                context.startActivity(
                                                    Intent.createChooser(shareIntent, "Share recipe via")
                                                )
                                            }
                                        },
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "Share",
                                            modifier = Modifier.size(30.dp),
                                            tint = colorResource(R.color.primary)
                                        )
                                    }

                                    // Edit button (if author)
                                    if (d.createdById == ui.currentUserId) {
                                        IconButton(
                                            onClick = { onEdit(d) },
                                            modifier = Modifier.size(38.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit",
                                                modifier = Modifier.size(30.dp),
                                                tint = colorResource(R.color.primary)
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(8.dp))

                        // Image carousel
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(d.images) { url ->
                                AsyncImage(
                                    model = url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title & byline + bookmark
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(Modifier.fillMaxWidth(0.85f)) {
                                Text(
                                    text = d.category,
                                    fontFamily = poppinsFamily,
                                    fontSize = 13.sp,
                                    color = colorResource(R.color.primary),
                                    modifier = Modifier
                                        .background(shape = RoundedCornerShape(size = 100.dp), color = colorResource(R.color.secondary))
                                        .padding(horizontal = 14.dp, vertical = 2.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = d.title,
                                    fontFamily = poppinsFamily,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 32.sp
                                )
                            }
                            IconButton(
                                onClick = { viewModel.onToggleBookmark(d) },
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = if (ui.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    modifier = Modifier.size(42.dp),
                                    tint = if (ui.isBookmarked) colorResource(R.color.primary) else colorResource(R.color.gray),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats row
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                Modifier.width(68.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = colorResource(R.color.darkGray),
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    "${d.likes} mins", // TODO: use real duration
                                    color = colorResource(R.color.darkGray),
                                    fontFamily = poppinsFamily,
                                    fontSize = 14.sp
                                )
                            }
                            Column(
                                Modifier.width(68.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = colorResource(R.color.darkGray),
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    "512 cal", // TODO: use real calories
                                    color = colorResource(R.color.darkGray),
                                    fontFamily = poppinsFamily,
                                    fontSize = 14.sp
                                )
                            }
                            Column(
                                Modifier.width(68.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    Icons.Default.ThumbUpOffAlt,
                                    contentDescription = null,
                                    tint = colorResource(R.color.darkGray),
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    "${d.likes}",
                                    color = colorResource(R.color.darkGray),
                                    fontFamily = poppinsFamily,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Instructions
                        Text(
                            "Instructions",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppinsFamily,
                            fontSize = 22.sp,
                            color = colorResource(R.color.black)
                        )
                        Text(
                            d.instructions,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp,
                            color = colorResource(R.color.darkGray)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ingredients
                        Text(
                            "Ingredients",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppinsFamily,
                            fontSize = 22.sp,
                            color = colorResource(R.color.black)
                        )
                        d.ingredients.forEach { ing ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { /* TODO: onIngredientClick(ing.id) */ },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = ing.image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(MaterialTheme.shapes.small)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    ing.name,
                                    fontFamily = poppinsFamily,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "${ing.quantity} ${ing.unit}",
                                    fontFamily = poppinsFamily
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "By ${d.createdByName}",
                            fontFamily = poppinsFamily,
                            fontSize = 13.sp,
                            color = colorResource(R.color.gray)
                        )
                    }
                }
            }
        }
    }
}
