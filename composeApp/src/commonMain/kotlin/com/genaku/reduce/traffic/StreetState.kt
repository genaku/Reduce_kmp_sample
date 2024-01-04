package com.genaku.reduce.traffic

import com.genaku.reduce.State

sealed class StreetState : State {
    object Empty : StreetState()
    class Traffic(val cars: Int) : StreetState()

    operator fun plus(cars: Int): StreetState = when (this) {
        Empty -> Traffic(cars)
        is Traffic -> Traffic(this.cars + cars)
    }

    operator fun minus(cars: Int): StreetState = when (this) {
        Empty -> this
        is Traffic -> {
            val newCars = this.cars - cars
            if (newCars > 0) Traffic(newCars) else Empty
        }
    }

    val value: Int
        get() = when (this) {
            Empty -> 0
            is Traffic -> this.cars
        }

    override fun toString(): String = value.toString()
}