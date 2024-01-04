package com.genaku.reduce.traffic

import com.genaku.reduce.SuspendSideEffect
import com.genaku.reduce.suspendKnot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class Street(private val delay: Long) {

    private var _trafficLight: TrafficLight? = null

    private val knot = suspendKnot<StreetState, StreetIntent> {
        initialState = StreetState.Empty

        reduce { intent ->
            when (this) {
                StreetState.Empty -> when (intent) {
                    StreetIntent.Minus -> stateOnly
                    StreetIntent.Plus -> StreetState.Traffic(1) + outStreet()
                }
                is StreetState.Traffic -> when (intent) {
                    StreetIntent.Minus -> (this - 1).stateOnly
                    StreetIntent.Plus -> this + 1 + outStreet()
                }
            }
        }
    }

    private fun outStreet(): SuspendSideEffect<StreetIntent> = SuspendSideEffect {
        delay(delay)
        _trafficLight?.addCar()
        null
    }

    val state
        get() = knot.state

    fun start(coroutineScope: CoroutineScope) {
        knot.start(coroutineScope)
    }

    fun setTrafficLight(trafficLight: TrafficLight) {
        _trafficLight = trafficLight
    }

    fun carIn() {
        knot.offerIntent(StreetIntent.Plus)
    }

    fun carOut() {
        knot.offerIntent(StreetIntent.Minus)
    }
}