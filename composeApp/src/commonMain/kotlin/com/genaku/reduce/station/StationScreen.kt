package com.genaku.reduce.station

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.genaku.reduce.theme.AppTheme

class StationScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { StationScreenModel() }
        val navigator = LocalNavigator.currentOrThrow
        val state = screenModel.state.collectAsState()
        AppTheme {
            Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            screenModel.switch()
                        }
                    ) {
                        Text(text = "Switch transport")
                    }
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            navigator.pop()
                        }
                    ) {
                        Text(text = "Exit")
                    }
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    text = state.value.getValue()
                )
            }
        }
    }
}