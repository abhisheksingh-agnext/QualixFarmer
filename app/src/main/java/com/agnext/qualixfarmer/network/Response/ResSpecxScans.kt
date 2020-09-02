package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResSpecxScans
{

    @SerializedName("id")
    var id: String? = null

    @SerializedName("weight")
    var weight: String? = null

    @SerializedName("bar_code")
    var bar_code: String? = null

    @SerializedName("commodity_name")
    var commodity_name: String? = null

    @SerializedName("created_on")
    var created_on: String? = null

    @SerializedName("total_count")
    var total_count: String? = null

    @SerializedName("reference_id")
    var reference_id: String? = null
}