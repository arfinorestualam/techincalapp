package com.fin.technicalapp.core.data.authentication.implemantion.repository

import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.authentication.api.model.Login
import com.fin.technicalapp.core.data.authentication.api.repository.AuthenticationRepository
import com.fin.technicalapp.core.data.authentication.implemantion.mapper.toLogin
import com.fin.technicalapp.core.data.authentication.implemantion.remote.AuthenticationApi
import com.fin.technicalapp.core.data.authentication.implemantion.request.LoginRequest
import com.fin.technicalapp.core.stash.api.Stash
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(private val auth: AuthenticationApi, private val stash: Stash): AuthenticationRepository {

    companion object {
        private const val KEY_USER = "KEY_USER"
    }

    override suspend fun login(loginRequest: LoginRequest): Flow<AppResponse<Login>> = flow {
        emit(AppResponse.Loading)
        try {
            val response = auth.login(loginRequest)
            when {
                response.token!!.isNotEmpty() -> {
                    emit(AppResponse.Success(response.toLogin()))
                } else -> {
                    emit(AppResponse.Error("server error"))
                }
            }
        } catch (e: Exception) {
            emit(AppResponse.Error("username or password is incorrect"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun saveUser(login: Login): Boolean = stash.save(KEY_USER, Gson().toJson(login))

    override suspend fun readUser(): Login {
        return withContext(Dispatchers.IO) {
            val json = stash.read(KEY_USER, "")
            if (json.isEmpty()) return@withContext Login()
            Gson().fromJson(json, Login::class.java)
        }
    }

    override suspend fun clearUser(): Boolean = stash.remove(KEY_USER)

}