package com.genaku.reduce.traffic

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun TrafficView(screenModel: TrafficScreenModel, navigator: Navigator) {
    val street1State = screenModel.street1State.collectAsState()
    val tl1State = screenModel.tl1State.collectAsState()
    val street2State = screenModel.street2State.collectAsState()
    val tl2State = screenModel.tl2State.collectAsState()
    val street3State = screenModel.street3State.collectAsState()
    val tl3State = screenModel.tl3State.collectAsState()
    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    screenModel.addCar()
                }
            ) {
                Text(text = "Add car")
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
            text = "Total: ${street1State.value.value + street2State.value.value + street3State.value.value} cars"
        )
        StreetView(1, street1State.value)
        TrafficLightView(1, tl1State.value)
        StreetView(2, street2State.value)
        TrafficLightView(2, tl2State.value)
        StreetView(3, street3State.value)
        TrafficLightView(3, tl3State.value)
    }
}

@Composable
fun StreetView(idx: Int, state: StreetState) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = "Street $idx: $state cars"
    )
}

@Composable
fun TrafficLightView(idx: Int, state: TrafficState) {
    Text(
        modifier = Modifier.padding(8.dp),
        fontStyle = FontStyle.Italic,
        text = "Traffic light $idx is $state"
    )
}
