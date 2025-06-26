package com.jenny.mealmate.presentation.create_recipe

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jenny.mealmate.R
import com.jenny.mealmate.ui.theme.poppinsFamily
import com.jenny.mealmate.util.ImageUtils

@Composable
fun CreateOrEditRecipeScreen(
    viewModel: CreateRecipeViewModel,
    recipeId: String?,
    onBack: () -> Unit,
    onFinished: () -> Unit,
    onDeleted: () -> Unit
) {
    val ui by viewModel.ui.collectAsState()
    val scroll = rememberScrollState()
    val deleting = ui.isDeleting

    // launchers
    val context = LocalContext.current
    val pickImage = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let {
           // convert to Base64-data-URI before storing in your list
            val dataUri = ImageUtils.uriToBase64DataUri(context, it)
            viewModel.onImagePicked(ui.images.size, dataUri)
        }
    }
    val pickVideo = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let { viewModel.onVideoPicked(it.toString()) }
    }

    Scaffold(containerColor = colorResource(R.color.bgColor)) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (ui.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(R.color.primary))
                }
                return@Scaffold
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.bgColor))
                    .verticalScroll(scroll)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val saving = ui.isSaving


                // Top bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // typical AppBar height
                    contentAlignment = Alignment.Center
                ) {
                    // Centered Title
                    Text(
                        text = if (ui.isEditMode) "Edit recipe" else "Create recipe",
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
                            enabled = !saving,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = colorResource(R.color.black),
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        if (saving)
                            CircularProgressIndicator(color = colorResource(R.color.primary), modifier = Modifier.size(24.dp))
                        else
                            Text(
                                text = "Done",
                                color = colorResource(R.color.primary),
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        viewModel.onConfirm { onFinished() }
                                    },
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                    }
                }


                Spacer(Modifier.height(2.dp))

                // Upload photos
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Upload photos", fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            Box(
                                Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colorResource(R.color.secondary))
                                    .clickable(enabled = !saving) { pickImage.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add photo", tint = colorResource(R.color.black))
                            }
                        }
                        itemsIndexed(ui.images) { idx, uri ->
                            Box(
                                Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colorResource(R.color.secondary)),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                if (uri.startsWith("data:")) {
                                    val base64 = uri.substringAfter("base64,")
                                    val bytes = Base64.decode(base64, Base64.DEFAULT)
                                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    Image(
                                        bitmap = bmp.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.onRemoveImage(idx) },
                                    enabled = !saving,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red)
                                }
                            }
                        }
                    }
                    ui.photoError?.let { err ->
                        Text(
                            text = err,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp),
                            fontFamily = poppinsFamily
                        )
                    }
                }


                Spacer(Modifier.height(0.dp))

                // Upload video
//                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
//                    Text("Upload video (Optional)", fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)
//                    Box(
//                        Modifier
//                            .fillMaxWidth()
//                            .height(160.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                            .background(colorResource(R.color.secondary))
//                            .clickable(enabled = !saving) { pickVideo.launch("video/*") },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        if (ui.videoUri.isBlank()) {
//                            Icon(Icons.Default.Upload, contentDescription = "Pick video", tint = colorResource(R.color.darkGray))
//                        } else {
//                            Text("Video selected", color = colorResource(R.color.black))
//                            IconButton (
//                                onClick = { viewModel.onVideoPicked("") },
//                                enabled = !saving,
//                                modifier = Modifier.align(Alignment.TopEnd)
//                            ) {
//                                Icon(
//                                    Icons.Default.Delete,
//                                    contentDescription = "Remove video",
//                                    tint = colorResource(R.color.errorColor)
//                                )
//                            }
//                        }
//                    }
//                }



//                Spacer(Modifier.height(0.dp))

                // Title
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Title", fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)

                    TextField(
                        value = ui.title,
                        onValueChange = viewModel::onTitleChanged,
                        enabled = !saving,
                        placeholder = {
                            Text(
                                "E.g. Chicken Curry",
                                fontFamily = poppinsFamily,
                                color = colorResource(R.color.gray)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.secondary),
                            unfocusedContainerColor = colorResource(R.color.secondary),
                            disabledContainerColor = colorResource(R.color.secondary),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp,
                            color = colorResource(R.color.black)
                        )
                    )

                    ui.titleError?.let {
                        Text(
                            it,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp),
                            fontFamily = poppinsFamily
                        )
                    }
                }

                Spacer(Modifier.height(0.dp))


                // Category
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Category", fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)
                    CategorySelector(
                        categories = ui.categories,
                        selectedCategoryId = ui.selectedCategory,
                        onCategorySelected = viewModel::onCategorySelected,
                        enabled = !saving,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ui.categoryError?.let { Text(it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(start = 8.dp), fontFamily = poppinsFamily) }
                }



                Spacer(Modifier.height(0.dp))
                // Instructions
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Instructions", fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = ui.instructions,
                        onValueChange = viewModel::onInstructionsChanged,
                        enabled = !saving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 100.dp),
                        minLines = 5,
                        maxLines = Int.MAX_VALUE,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.secondary),
                            unfocusedContainerColor = colorResource(R.color.secondary),
                            disabledContainerColor = colorResource(R.color.secondary),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(fontFamily = poppinsFamily, fontSize = 16.sp, color = colorResource(R.color.black))
                    )
                    ui.instructionsError?.let { Text(it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(start = 8.dp), fontFamily = poppinsFamily) }
                }


                Spacer(Modifier.height(0.dp))
                // Ingredients


                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Ingredients", fontFamily = poppinsFamily, fontWeight = FontWeight.Medium)
                    ui.ingredientRows.forEachIndexed { idx, row ->
                        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            IngredientSelector(
                                ingredients = ui.ingredientsList,
                                selectedIngredientId = row.ingredientId,
                                onIngredientSelected = { id -> viewModel.onRowChanged(idx, row.copy(ingredientId = id)) },
                                enabled = !saving,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = row.quantity,
                                    onValueChange = { q -> viewModel.onRowChanged(idx, row.copy(quantity = q)) },
                                    placeholder = { Text("Qty", fontFamily = poppinsFamily, color = colorResource(R.color.gray)) },
                                    enabled = !saving,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = colorResource(R.color.secondary),
                                        unfocusedContainerColor = colorResource(R.color.secondary),
                                        disabledContainerColor = colorResource(R.color.secondary),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    textStyle = TextStyle(fontFamily = poppinsFamily, fontSize = 16.sp, color = colorResource(R.color.black))
                                )
                                TextField(
                                    value = row.unit,
                                    onValueChange = { u -> viewModel.onRowChanged(idx, row.copy(unit = u)) },
                                    placeholder = { Text("Unit", fontFamily = poppinsFamily, color = colorResource(R.color.gray)) },
                                    enabled = !saving,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = colorResource(R.color.secondary),
                                        unfocusedContainerColor = colorResource(R.color.secondary),
                                        disabledContainerColor = colorResource(R.color.secondary),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent
                                    ),
                                    textStyle = TextStyle(fontFamily = poppinsFamily, fontSize = 16.sp, color = colorResource(R.color.black))
                                )
                                IconButton(
                                    onClick = { viewModel.onRemoveRow(idx) },
                                    enabled = !saving
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove ingredient", tint = colorResource(R.color.errorColor))
                                }
                            }
                            if (ui.rowErrors.getOrNull(idx) == true) {
                                Text(
                                    "Please fill out all ingredient fields",
                                    color = Color.Red,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 5.dp),
                                    fontFamily = poppinsFamily
                                )
                            }
                        }
                    }
                }

                // Add ingredient row
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    IconButton(onClick = viewModel::onAddRow, enabled = !saving) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Ingredient", tint = colorResource(R.color.primary))
                    }
                }

                if (ui.isEditMode) {
                    Button(
                        onClick = { viewModel.onDelete { onDeleted() } },
                        enabled = !saving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC6C6),
                            contentColor = Color.Red,
                            disabledContentColor = Color.Red,
                            disabledContainerColor = Color(0xFFFFC6C6),
                        )
                    ) {
                        Text(
                            text = "Delete this recipe",
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp
                        )
                    }
                }

            }
        }
        if (deleting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.primary))
            }
        }

    }
}
