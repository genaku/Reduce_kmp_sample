package com.genaku.reduce.sms

interface ISmsRepository {
    suspend fun checkSms(sms: String): Boolean
}