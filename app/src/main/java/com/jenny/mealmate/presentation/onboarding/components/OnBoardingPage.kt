package com.jenny.mealmate.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jenny.mealmate.R
import com.jenny.mealmate.presentation.onboarding.Page
import com.jenny.mealmate.presentation.onboarding.pages
import com.jenny.mealmate.ui.theme.MealmateTheme
import com.jenny.mealmate.ui.theme.poppinsFamily

@Composable
fun OnBoardingPage (
    modifier: Modifier = Modifier,
    page: Page
) {
    Column (
        modifier = modifier.fillMaxSize().padding(bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            modifier = Modifier,
            painter = painterResource(id = page.image),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = page.title,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFamily,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = page.desc,
            textAlign = TextAlign.Center,
            fontFamily = poppinsFamily,
            fontSize = 16.sp,
            color = colorResource(id = R.color.black)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OnBoardingPagePreview () {
    MealmateTheme {
        OnBoardingPage(
            page = pages[2]
        )
    }
}