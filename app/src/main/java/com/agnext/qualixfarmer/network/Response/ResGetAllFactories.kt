package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResGetAllFactories {
    @SerializedName("success")
    var success: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data = ArrayList<allFacories>()
}

class allFacories{
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
}