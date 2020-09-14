package com.agnext.sensenextmyadmin.ui.auth.login

import com.agnext.qualixfarmer.network.Response.OauthResponse

enum class LoginState {
    Success,
    EmptyUserName,
    EmptyPassword,
    ErrorServer,
    LoginConnectionError,

    AuthSuccess,
    AuthFailure,
    QualixLoginSuccess,
    QualixLoginFailure,

    VMSLoginSuccess,
    VMSLoginFailure,

    TokenExpire
}