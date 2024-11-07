package com.fin.technicalapp.core.data.authentication.implemantion.remote

import com.fin.technicalapp.core.data.authentication.implemantion.request.LoginRequest
import com.fin.technicalapp.core.data.authentication.implemantion.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): LoginResponse
}