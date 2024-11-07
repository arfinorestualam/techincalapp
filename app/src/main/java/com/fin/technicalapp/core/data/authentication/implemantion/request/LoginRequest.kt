package com.fin.technicalapp.core.data.authentication.implemantion.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("username")
    var username: String = "",
    @SerializedName("password")
    var password: String = "",
)