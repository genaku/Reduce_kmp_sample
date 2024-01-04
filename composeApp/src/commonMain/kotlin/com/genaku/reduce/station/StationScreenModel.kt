package com.genaku.reduce.station

import cafe.adriel.voyager.core.model.ScreenModel
import com.genaku.reduce.CoroutineKnotState
import com.genaku.reduce.StateIntent
import com.genaku.reduce.SuspendSideEffect
import com.genaku.reduce.suspendKnot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class StationScreenModel : ScreenModel {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val stationState = CoroutineKnotState<StationState>(StationState.Empty)

    private val arrive = " arrive"
    private val departure = " departure"

    private class Vehicle(val name: String, val delay: Long, var num: Int = 0) {
        override fun toString(): String = "$name $num"
    }

    private suspend fun <I : StateIntent> arrive(vehicle: Vehicle, create: (Vehicle) -> I) = SuspendSideEffect {
        delay(vehicle.delay)
        vehicle.num++
        create(vehicle)
    }

    private suspend fun <I : StateIntent> leave(vehicle: Vehicle, create: (Vehicle) -> I) = SuspendSideEffect {
        delay(vehicle.delay)
        create(vehicle)
    }

    private val busKnot = suspendKnot<StationState, BusIntent> {
        knotState = stationState

        val bus = Vehicle("Bus", 800)

        reduce { intent ->
            when (intent) {
                is BusIntent.Arrive -> StationState.Bus(intent.name + arrive) +
                        leave(bus) { BusIntent.Leave("$it") }

                is BusIntent.Leave -> StationState.Bus(intent.name + departure) +
                        arrive(bus) { BusIntent.Arrive("$it") }
            }
        }
    }

    private val trainKnot = suspendKnot<StationState, TrainIntent> {
        knotState = stationState

        val train = Vehicle("Train", 600)

        reduce { intent ->
            when (intent) {
                is TrainIntent.Arrive -> StationState.Train(intent.name + arrive) +
                        leave(train) { TrainIntent.Leave("$it") }

                is TrainIntent.Leave -> StationState.Train(intent.name + departure) +
                        arrive(train) { TrainIntent.Arrive("$it") }
            }
        }
    }

    private val lorryKnot = suspendKnot<StationState, LorryIntent> {
        knotState = stationState
        val lorry = Vehicle("Lorry", 250)

        reduce { intent ->
            when (intent) {
                is LorryIntent.Arrive -> StationState.Lorry(intent.name + arrive) +
                        leave(lorry) { LorryIntent.Leave("$it") }

                is LorryIntent.Leave -> StationState.Lorry(intent.name + departure) +
                        arrive(lorry) { LorryIntent.Arrive("$it") }
            }
        }
    }

    private var case = 0

    val state: StateFlow<StationState>
        get() = stationState.state

    fun switch() {
        when (case) {
            0 -> {
                lorryKnot.stop()
                trainKnot.stop()
                busKnot.start(coroutineScope)
                busKnot.offerIntent(BusIntent.Arrive("start"))
            }

            1 -> {
                busKnot.stop()
                trainKnot.stop()
                lorryKnot.start(coroutineScope)
                lorryKnot.offerIntent(LorryIntent.Arrive("start"))
            }

            2 -> {
                lorryKnot.stop()
                busKnot.stop()
                trainKnot.start(coroutineScope)
                trainKnot.offerIntent(TrainIntent.Arrive("start"))
            }

            else -> {
                case = -1
                stop()
            }
        }
        case++
    }

    private fun stop() {
        busKnot.stop()
        lorryKnot.stop()
        trainKnot.stop()
    }

    override fun onDispose() {
        stop()
        super.onDispose()
    }
}