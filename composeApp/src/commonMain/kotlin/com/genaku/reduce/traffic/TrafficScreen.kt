package com.genaku.reduce.traffic

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.genaku.reduce.theme.AppTheme

class TrafficScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { TrafficScreenModel() }
        val navigator = LocalNavigator.currentOrThrow
        AppTheme {
            TrafficView(screenModel, navigator)
        }
    }
}