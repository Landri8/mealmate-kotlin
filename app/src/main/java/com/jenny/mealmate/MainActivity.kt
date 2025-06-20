package com.jenny.mealmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jenny.mealmate.presentation.common.toaster.ToasterHost
import com.jenny.mealmate.presentation.navgraph.NavGraph
import com.jenny.mealmate.ui.theme.MealmateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.splashCondition
            }
        }

        setContent {
            MealmateTheme {
                ToasterHost()

                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()

                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }

                val startDestination = viewModel.startDestination
                NavGraph(startDestination = startDestination)

            }
        }
    }
}
