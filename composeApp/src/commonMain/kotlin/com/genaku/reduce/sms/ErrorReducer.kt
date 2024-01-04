package com.genaku.reduce.sms

import com.genaku.reduce.JobSwitcher
import com.genaku.reduce.StateAction
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class ErrorReducer(dispatcher: CoroutineContext = Dispatchers.Default) : JobSwitcher, IErrorInteractor {

    private val errorKnot = knot<ErrorState, ErrorIntent, StateAction> {
        dispatcher(dispatcher)

        initialState = ErrorState.NoError

        reduce { intent ->
            when (intent) {
                ErrorIntent.ClearError -> ErrorState.NoError.stateOnly
                is ErrorIntent.SetError -> ErrorState.Error(intent.error).stateOnly
            }
        }
    }

    override val errorState: StateFlow<ErrorState>
        get() = errorKnot.state

    override fun processError(error: IError) {
        errorKnot.offerIntent(ErrorIntent.SetError(error))
    }

    override fun clearError() {
        errorKnot.offerIntent(ErrorIntent.ClearError)
    }

    override fun start(coroutineScope: CoroutineScope) {
        errorKnot.start(coroutineScope)
    }

    override fun stop() {
        errorKnot.stop()
    }
}