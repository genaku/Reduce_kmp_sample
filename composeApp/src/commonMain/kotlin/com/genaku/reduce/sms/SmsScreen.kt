package com.genaku.reduce.sms

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.genaku.reduce.theme.AppTheme

class SmsScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { SmsScreenModel() }
        val navigator = LocalNavigator.currentOrThrow
        AppTheme {
            SmsView(screenModel, navigator)
        }
    }
}