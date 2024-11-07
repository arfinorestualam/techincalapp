package com.fin.technicalapp.core.data.authentication.api.repository

import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.authentication.api.model.Login
import com.fin.technicalapp.core.data.authentication.implemantion.request.LoginRequest
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(loginRequest: LoginRequest): Flow<AppResponse<Login>>

    suspend fun saveUser(login: Login): Boolean

    suspend fun readUser(): Login

    suspend fun clearUser(): Boolean
}