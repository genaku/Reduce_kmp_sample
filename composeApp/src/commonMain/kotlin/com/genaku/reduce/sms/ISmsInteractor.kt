package com.genaku.reduce.sms

import kotlinx.coroutines.flow.StateFlow

interface ISmsInteractor {
    val state: StateFlow<SmsState>

    fun checkSms(sms: String)
    fun cancel()
}