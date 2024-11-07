package com.fin.technicalapp.core.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

fun <T> AppCompatActivity.collectFlow(flow: Flow<T>, action: ((T) -> Unit)) {
    getLaunch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                action.invoke(it)
            }
        }
    }
}