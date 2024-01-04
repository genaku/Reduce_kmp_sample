package com.genaku.reduce.sms

import com.genaku.reduce.StateAction
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class LoadingReducer(private val errorReducer: ErrorReducer) : ILoadingInteractor,
    IErrorInteractor by errorReducer {

    private val loadingKnot = knot<LoadingState, LoadingIntent, StateAction> {
        initialState = LoadingState.Idle

        reduce { intent ->
            when (intent) {
                LoadingIntent.Start -> LoadingState.Active.stateOnly
                LoadingIntent.Stop -> LoadingState.Idle.stateOnly
            }
        }
    }

    override val loadingState: StateFlow<LoadingState>
        get() = loadingKnot.state

    override fun start(coroutineScope: CoroutineScope) {
        loadingKnot.start(coroutineScope)
        errorReducer.start(coroutineScope)
    }

    override fun stop() {
        loadingKnot.stop()
        errorReducer.stop()
    }

    override suspend fun <T> processWrap(default: T, block: suspend () -> T): T {
        loadingKnot.offerIntent(LoadingIntent.Start)
        var result = default
        try {
            result = block()
        } catch (e: Exception) {
            errorReducer.processError(ErrorData("error"))
        } finally {
            loadingKnot.offerIntent(LoadingIntent.Stop)
        }
        return result
    }
}