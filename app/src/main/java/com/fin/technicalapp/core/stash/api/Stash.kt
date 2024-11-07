package com.fin.technicalapp.core.stash.api

interface Stash {
    suspend fun <T> read(key: String, defaultValue: T): T

    suspend fun <T> save(key: String, value: T): Boolean

    suspend fun clear(): Boolean

    suspend fun remove(key: String): Boolean
}