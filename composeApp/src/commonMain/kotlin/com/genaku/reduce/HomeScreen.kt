package com.genaku.reduce

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.genaku.reduce.sms.SmsScreen
import com.genaku.reduce.station.StationScreen
import com.genaku.reduce.theme.AppTheme
import com.genaku.reduce.traffic.TrafficScreen

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AppTheme {
            Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp)) {
                Button(onClick = {
                    navigator.push(TrafficScreen())
                }) {
                    Text("Traffic")
                }
                Button(onClick = {
                    navigator.push(StationScreen())
                }) {
                    Text("Station")
                }
                Button(onClick = {
                    navigator.push(SmsScreen())
                }) {
                    Text("Sms")
                }

            }
        }
    }
}