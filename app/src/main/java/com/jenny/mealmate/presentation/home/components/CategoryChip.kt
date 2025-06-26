
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.domain.model.CategorySection
import com.jenny.mealmate.ui.theme.poppinsFamily

@Composable
fun CategoryChipsRow(
    categories: List<CategorySection>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { cat ->
            val isSelected = cat.categoryId == selectedCategory

            TextButton(
                onClick = { onCategorySelected(cat.categoryId) },
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected)
                        colorResource(id = R.color.secondary)
                    else
                        Color.White,
                    contentColor = if (isSelected)
                        colorResource(id = R.color.primary)
                    else
                        Color.Black
                ),
            ) {
                Text(
                    text = cat.categoryName,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontFamily = poppinsFamily,
                    fontSize = 15.sp
                )
            }
        }
    }
}
