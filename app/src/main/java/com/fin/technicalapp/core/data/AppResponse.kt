package com.fin.technicalapp.core.data

sealed interface AppResponse<out T> {
    object Loading : AppResponse<Nothing>

    open class Error(
        open val message: String,
        val meta: Map<String, Any?> = mapOf(),
    ) : AppResponse<Nothing>

    object Empty : AppResponse<Nothing>

    open class Success<T>(
        val data: T,
        val meta: Map<String, Any?> = mapOf(),
    ): AppResponse<T>
}