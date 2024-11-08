package com.fin.technicalapp.feature.login

import androidx.lifecycle.ViewModel
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.authentication.api.model.Login
import com.fin.technicalapp.core.data.authentication.api.repository.AuthenticationRepository
import com.fin.technicalapp.core.data.authentication.implemantion.request.LoginRequest
import com.fin.technicalapp.core.utils.extensions.getLaunch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll

class LoginViewModel(val authenticationRepository: AuthenticationRepository): ViewModel() {
    private val _login = MutableSharedFlow<AppResponse<Login>>()
    val login = _login.asSharedFlow()

    var loginRequest = LoginRequest()

    fun login() {
        getLaunch {
            _login.emitAll(authenticationRepository.login(loginRequest))
        }
    }

    suspend fun saveUserData(token: String): Boolean = authenticationRepository.saveUser(Login(username = loginRequest.username, token =  token))

    suspend fun readUserData(): Login = authenticationRepository.readUser()

    suspend fun clearUserData(): Boolean = authenticationRepository.clearUser()
}