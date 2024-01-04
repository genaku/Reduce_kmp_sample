package com.genaku.reduce.traffic

import com.genaku.reduce.StateIntent

sealed class StreetIntent : StateIntent {
    data object Plus : StreetIntent()
    data object Minus : StreetIntent()
}