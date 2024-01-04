package com.genaku.reduce.sms

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow

class SmsScreenModel : ScreenModel {

    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val loadingUseCase = DI.loadingReducer
    val smsReducer = SmsReducer(DI.repository, loadingUseCase, coroutineScope)

    val loadingState: StateFlow<LoadingState>
        get() = loadingUseCase.loadingState

    val errorState: StateFlow<ErrorState>
        get() = loadingUseCase.errorState

    init {
        loadingUseCase.start(coroutineScope)
        smsReducer.start(coroutineScope)
    }

    override fun onDispose() {
        smsReducer.stop()
        loadingUseCase.stop()
        coroutineScope.cancel()
        super.onDispose()
    }
}