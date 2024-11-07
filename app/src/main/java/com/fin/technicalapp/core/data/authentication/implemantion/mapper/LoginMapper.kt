package com.fin.technicalapp.core.data.authentication.implemantion.mapper

import com.fin.technicalapp.core.data.authentication.api.model.Login
import com.fin.technicalapp.core.data.authentication.implemantion.response.LoginResponse

fun LoginResponse.toLogin() = Login(
    token = this.token?: "",
)