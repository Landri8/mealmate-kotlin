package com.jenny.mealmate.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jenny.mealmate.presentation.common.GeneralButton
import com.jenny.mealmate.presentation.common.GeneralTextButton
import com.jenny.mealmate.presentation.onboarding.components.OnBoardingPage
import com.jenny.mealmate.presentation.onboarding.components.PageIndicator
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen (
    event: (OnBoardingEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val pagerState = rememberPagerState (initialPage = 0) {
            pages.size
        }

        val buttonState = remember {
            derivedStateOf {
                when(pagerState.currentPage) {
                    0 -> listOf("", "Next")
                    1 -> listOf("Back", "Next")
                    2 -> listOf("Back", "Get Started")
                    else -> listOf("", "")
                }
            }
        }

        HorizontalPager(state = pagerState) { index ->
            OnBoardingPage(page = pages[index])
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            PageIndicator(
                modifier = Modifier.width(56.dp),
                pageSize = pages.size,
                selectedPage = pagerState.currentPage
            )

            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val scope = rememberCoroutineScope()

                if (buttonState.value[0].isNotEmpty()) {
                    GeneralTextButton(
                        text = buttonState.value[0],
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                GeneralButton(
                    modifier = Modifier,
                    text = buttonState.value[1],
                    onClick = {
                        if (pagerState.currentPage == 2) {
                            event(OnBoardingEvent.SaveAppEntry)
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                            }
                        }
                    }
                )
            }
        }
    }
}