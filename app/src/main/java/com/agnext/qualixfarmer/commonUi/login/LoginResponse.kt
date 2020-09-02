package com.agnext.sensenextmyadmin.ui.auth.login

import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: Boolean? = null
    @SerializedName("token")
    var token: String? = null
    @SerializedName("refreshToken")
    var refreshToken: String? = null
    @SerializedName("user")
    var user: User? = null

}

class User {

    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("username")
    var userName: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("createdOn")
    var createdOn: String? = null
    @SerializedName("statusId")
    var statusId: Int? = null
    @SerializedName("status")
    var status: String? = null

    @SerializedName("baseLocation")
    var baseLocation: String? = null
    @SerializedName("companyId")
    var companyId: Int? = null
    @SerializedName("companyName")
    var companyName: String? = null
    @SerializedName("code")
    var code: Int? = null
    @SerializedName("expiredOn")
    var expiredOn: String? = null
    @SerializedName("roles ")
    var roles: List<Role>? = null

}

class Role {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
}

