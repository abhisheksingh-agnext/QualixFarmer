package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class UserData {

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("profile")
    var profile: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("contact_number")
    var contactNumber: String? = null

    @SerializedName("customer_id")
    var customerId: String? = null

    @SerializedName("roles")
    var roles: ArrayList<String>? = null
}