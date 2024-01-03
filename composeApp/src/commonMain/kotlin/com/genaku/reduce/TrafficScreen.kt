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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.genaku.reduce.theme.AppTheme
import com.genaku.reduce.traffic.Street
import com.genaku.reduce.traffic.TrafficLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TrafficScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { TrafficScreenModel() }
        AppTheme {
            Traffic(screenModel)
        }
    }
}

@Composable
fun Traffic(screenModel: TrafficScreenModel) {
    val street1State = screenModel.street1State.collectAsState()
    val tl1State = screenModel.tl1State.collectAsState()
    val street2State = screenModel.street2State.collectAsState()
    val tl2State = screenModel.tl2State.collectAsState()
    val street3State = screenModel.street3State.collectAsState()
    val tl3State = screenModel.tl3State.collectAsState()
    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp)) {
        Button(
            onClick = {
                screenModel.addCar()
            }
        ) {
            Text(text = "Add car")
        }
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Street 1: ${street1State.value} cars"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Traffic light 1 is ${tl1State.value}"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Street 2: ${street2State.value} cars"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Traffic light 2 is ${tl2State.value}"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Street 3: ${street3State.value} cars"
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Traffic light 3 is ${tl3State.value}"
        )
    }
}

class TrafficScreenModel : ScreenModel {

    private val street1 = Street(100)
    private val street2 = Street(200)
    private val street3 = Street(300)

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val tl1 = TrafficLight(
        coroutineScope = coroutineScope,
        delay = 30,
        limit = 10
    )
    private val tl2 = TrafficLight(
        coroutineScope = coroutineScope,
        delay = 20,
        limit = 5
    )
    private val tl3 = TrafficLight(
        coroutineScope = coroutineScope,
        delay = 10
    )

    init {
        initStreets()
        start()
    }

    private fun initStreets() {
        street1.setTrafficLight(tl1)
        street2.setTrafficLight(tl2)
        street3.setTrafficLight(tl3)

        tl1.setStreetIn(street1)
        tl1.setStreetOut(street2)
        tl2.setStreetIn(street2)
        tl2.setStreetOut(street3)
        tl3.setStreetIn(street3)
        tl3.setStreetOut(street1)
    }

    private fun start() {
        street1.start(coroutineScope)
        street2.start(coroutineScope)
        street3.start(coroutineScope)
        tl1.start()
        tl2.start()
        tl3.start()
    }

    val street1State
        get() = street1.state

    val street2State
        get() = street2.state

    val street3State
        get() = street3.state

    val tl1State
        get() = tl1.state

    val tl2State
        get() = tl2.state

    val tl3State
        get() = tl3.state

    fun addCar() {
        street1.carIn()
    }
}