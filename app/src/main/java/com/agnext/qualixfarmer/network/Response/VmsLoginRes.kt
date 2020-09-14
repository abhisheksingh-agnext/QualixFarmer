package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class VmsLoginRes
{
    @SerializedName("id")
    var id: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("contact_number")
    var contactNumber: String? = null
    @SerializedName("role")
    var role: String? = null
    @SerializedName("createdOn")
    var createdOn: String? = null
    @SerializedName("status_id")
    var statusId: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("token")
    var token: String? = null
    @SerializedName("account_number")
    var account_number: String? = null


}