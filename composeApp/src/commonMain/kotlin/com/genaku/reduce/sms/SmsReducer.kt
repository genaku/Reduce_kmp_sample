package com.genaku.reduce.sms

import com.genaku.reduce.JobSwitcher
import com.genaku.reduce.StateIntent
import com.genaku.reduce.SuspendSideEffect
import com.genaku.reduce.suspendKnot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class SmsReducer(
    private val repository: ISmsRepository,
    private val loadingReducer: LoadingReducer,
    private val useCaseCoroutineScope: CoroutineScope
) : ISmsInteractor, JobSwitcher {

    private val smsKnot = suspendKnot<SmsState, SmsIntent> {

        initialState = SmsState.InputSms

        reduce { intent ->
            println("reduce $this by $intent")
            when (this) {
                SmsState.InputSms -> when (intent) {
                    SmsIntent.Cancel -> SmsState.Exit.stateOnly
                    is SmsIntent.SendSms -> SmsState.CheckSms + sendSms(intent.sms)
                    is SmsIntent.WrongSms -> {
                        loadingReducer.processError(ErrorData("Code is wrong"))
                        this.stateOnly
                    }

                    else -> unexpected(intent)
                }

                SmsState.CheckSms -> when (intent) {
                    SmsIntent.CorrectSms -> SmsState.SmsConfirmed.stateOnly
                    SmsIntent.WrongSms -> {
                        loadingReducer.processError(ErrorData("Code is wrong"))
                        SmsState.InputSms.stateOnly
                    }

                    SmsIntent.Cancel -> SmsState.InputSms.stateOnly
                    else -> unexpected(intent)
                }

                SmsState.Exit -> {
                    stateOnly
                }

                SmsState.SmsConfirmed -> {
                    if (intent is SmsIntent.Cancel) {
                        SmsState.Exit.stateOnly
                    } else {
                        stateOnly
                    }
                }
            }
        }
    }.apply {
        start(useCaseCoroutineScope)
    }

    private suspend fun sendSms(sms: String) = SuspendSideEffect {
        if (wrappedCheckSms(sms)) SmsIntent.CorrectSms else SmsIntent.WrongSms
    }

    private suspend fun wrappedCheckSms(sms: String) =
        loadingReducer.processWrap(false) {
            repository.checkSms(sms)
        }

    override val state: StateFlow<SmsState>
        get() = smsKnot.state

    override fun start(coroutineScope: CoroutineScope) {
        smsKnot.start(coroutineScope)
    }

    override fun stop() {
        smsKnot.stop()
    }

    override fun checkSms(sms: String) {
        loadingReducer.clearError()
        smsKnot.offerIntent(SmsIntent.SendSms(sms))
    }

    override fun cancel() {
        smsKnot.offerIntent(SmsIntent.Cancel)
    }

    private sealed class SmsIntent : StateIntent {
        class SendSms(val sms: String) : SmsIntent()
        data object Cancel : SmsIntent()
        data object WrongSms : SmsIntent()
        data object CorrectSms : SmsIntent()
    }
}

