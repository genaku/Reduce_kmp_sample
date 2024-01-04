package com.genaku.reduce.sms

import com.genaku.reduce.JobSwitcher
import kotlinx.coroutines.flow.StateFlow

interface ILoadingInteractor : JobSwitcher {
    val loadingState: StateFlow<LoadingState>
    suspend fun <T> processWrap(default: T, block: suspend () -> T): T
}