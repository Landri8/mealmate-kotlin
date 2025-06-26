package com.jenny.mealmate.presentation.home

import CategoryChipsRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jenny.mealmate.R
import com.jenny.mealmate.domain.model.CategorySection
import com.jenny.mealmate.domain.model.Recipe
import com.jenny.mealmate.presentation.home.components.HorizontalSection
import com.jenny.mealmate.presentation.home.components.RecipeCard
import com.jenny.mealmate.ui.theme.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    firstName: String,
    recipes: List<Recipe>,
    recommendations: List<Recipe>,
    categories: List<CategorySection>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onCreateClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onProfileClick: () -> Unit,
    onRefresh: () -> Unit,
    // ← new:
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)

    Scaffold(
        containerColor = colorResource(R.color.bgColor),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateClick,
                containerColor = colorResource(R.color.primary),
                contentColor = colorResource(R.color.white),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        SwipeRefresh(
            state = swipeState,
            onRefresh = onRefresh,
            indicator = { s, trigger ->
                SwipeRefreshIndicator(
                    state = s,
                    refreshTriggerDistance = trigger,
                    // you can customize colors, etc
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // … your existing top‐bar “Menu / Settings” row …
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp), // typical AppBar height
                        contentAlignment = Alignment.Center
                    ) {

                        // Left and Right actions
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {}, modifier = Modifier.size(38.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    modifier = Modifier.size(38.dp),
                                    tint = colorResource(id = R.color.black)
                                )
                            }
                            IconButton(onClick = onBookmarkClick, modifier = Modifier.size(38.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Settings",
                                    modifier = Modifier.size(38.dp),
                                    tint = colorResource(id = R.color.black)
                                )
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(4.dp)) }

                // … greeting + profile row …
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                            Text(
                                "Hello, $firstName",
                                fontWeight = FontWeight.Light,
                                fontFamily = poppinsFamily,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.black)
                            )
                            Text(
                                "What would you like to cook today?",
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppinsFamily,
                                fontSize = 28.sp,
                                lineHeight = 32.sp,
                                color = colorResource(id = R.color.black)
                            )
                        }
                        IconButton(onClick = onProfileClick) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                modifier = Modifier.size(68.dp),
                                tint = colorResource(id = R.color.black)
                            )
                        }
                    }
                }

                // … search bar …
//                if (errorMessage == null) {
//                    item {
//                        val darkGray = colorResource(id = R.color.darkGray)
//                        OutlinedTextField(
//                            value = "",
//                            onValueChange = {},
//                            enabled = false,
//                            placeholder = {
//                                Text(
//                                    text = "Search for meals and recipes",
//                                    color = darkGray,
//                                    fontFamily = poppinsFamily,
//                                    fontSize = 16.sp
//                                )
//                            },
//                            leadingIcon = {
//                                Icon(
//                                    imageVector = Icons.Default.Search,
//                                    contentDescription = null,
//                                    tint = darkGray
//                                )
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp)
//                                .clickable { onSearchClick() }
//                                .background(Color.White)
//                        )
//                    }
//                }

                // ← NEW BLOCK: loading spinner or error message
                item {
                    if (isLoading) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = colorResource(id = R.color.primary)
                            )
                        }
                    }
                    errorMessage?.let { err ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Something went wrong, please try again later!",
                                fontFamily = poppinsFamily,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                if (!isLoading && errorMessage == null) {
                    item {
                        HorizontalSection(
                            title = "Your Recipes",
                            items = recipes,
                            onItemClick = onRecipeClick
                        )
                    }
                    item {
                        HorizontalSection(
                            title = "Recommendation",
                            items = recommendations,
                            onItemClick = onRecipeClick
                        )
                    }

                    // … categories title + chips …
                    item {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Categories",
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppinsFamily,
                            fontSize = 22.sp,
                            color = colorResource(id = R.color.black)
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    item {
                        CategoryChipsRow(
                            categories = categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = onCategorySelected
                        )
                        Spacer(Modifier.height(12.dp))
                    }


                    val section = categories.firstOrNull { it.categoryId == selectedCategory }
                    section?.recipes
                        ?.chunked(2)
                        ?.let { rows ->
                            items(rows) { rowRecipes ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowRecipes.forEach { recipe ->
                                        RecipeCard(
                                            recipe = recipe,
                                            onClick = { onRecipeClick(recipe) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowRecipes.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }

    }
}
