package com.genaku.reduce.traffic

import com.genaku.reduce.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class TrafficState : State {
    object On : TrafficState()
    object Off : TrafficState()

    override fun toString(): String = when (this) {
        On -> "green"
        Off -> "red"
    }
}

sealed class TrafficIntent : StateIntent {
    data object On : TrafficIntent()
    data object Off : TrafficIntent()
    data object Plus : TrafficIntent()
    data object Minus : TrafficIntent()
}

class TrafficLight(
    private val coroutineScope: CoroutineScope,
    private val delay: Long = 10,
    private val limit: Int = 20,
    private val lightTime: Long = 5000
) {

    private var streetIn: Street? = null
    private var streetOut: Street? = null

    private var cars = 0

    private var open = false
    private var moving = false

    private val knot = suspendKnot<TrafficState, TrafficIntent> {
        initialState = TrafficState.Off

        dispatcher(Dispatchers.Default)

        reduce { intent ->
            when (intent) {
                TrafficIntent.Minus -> {
                    if (cars > 0) {
                        cars--
                        this + startMovement()
                    } else {
                        stateOnly
                    }
                }

                TrafficIntent.Plus -> {
                    cars++
                    this + startMovement()
                }

                TrafficIntent.Off -> TrafficState.Off + close()
                TrafficIntent.On -> TrafficState.On + open() + startMovement()
            }
        }
    }

    val state
        get() = knot.state

    fun start() {
        knot.start(coroutineScope)
    }

    fun addCar() {
        cars++
        if (cars > limit) {
            coroutineScope.launch {
                knot.offerIntent(TrafficIntent.On)
                delay(lightTime)
                knot.offerIntent(TrafficIntent.Off)
            }
        }
    }

    fun setStreetIn(street: Street) {
        this.streetIn = street
    }

    fun setStreetOut(street: Street) {
        this.streetOut = street
    }

    private fun open() = SuspendSideEffect<TrafficIntent> {
        open = true
        null
    }

    private fun close() = SuspendSideEffect<TrafficIntent> {
        open = false
        null
    }

    private suspend fun startMovement() = SuspendSideEffect<TrafficIntent> {
        if (moving) return@SuspendSideEffect null
        moving = true
        while (open && cars > 0) {
            delay(delay)
            streetIn?.carOut()
            streetOut?.carIn()
            cars--
        }
        moving = false
        null
    }
}