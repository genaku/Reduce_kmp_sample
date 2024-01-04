package com.genaku.reduce.sms

import kotlinx.coroutines.flow.StateFlow

interface IErrorInteractor {
    val errorState: StateFlow<ErrorState>

    fun processError(error: IError)
    fun clearError()
}