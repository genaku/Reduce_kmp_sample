package com.genaku.reduce.sms

import kotlinx.coroutines.delay

object DI {

    val repository = object : ISmsRepository {
        override suspend fun checkSms(sms: String): Boolean {
            delay(500)
            return sms == "0101"
        }
    }

    val loadingReducer = LoadingReducer(ErrorReducer())
}