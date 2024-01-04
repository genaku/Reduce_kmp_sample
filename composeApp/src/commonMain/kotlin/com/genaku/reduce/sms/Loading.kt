package com.genaku.reduce.sms

import com.genaku.reduce.StateIntent
import com.genaku.reduce.State

sealed class LoadingState : State {
    data object Active: LoadingState()
    data object Idle: LoadingState()
}

sealed class LoadingIntent : StateIntent {
    data object Start: LoadingIntent()
    data object Stop: LoadingIntent()
}