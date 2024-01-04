package com.genaku.reduce.sms

import com.genaku.reduce.State

sealed class SmsState : State {
    data object InputSms : SmsState()
    data object CheckSms : SmsState()
    data object SmsConfirmed : SmsState()
    data object Exit : SmsState()
}