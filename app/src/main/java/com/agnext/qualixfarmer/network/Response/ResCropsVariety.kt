package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResCropsVariety
{
    @SerializedName("commodity_variety_id")
    var commodity_variety_id: String? = null
    @SerializedName("is_default_variety")
    var is_default_variety: Boolean? = null
    @SerializedName("variety_name")
    var variety_name: String? = null
    @SerializedName("commodity_id")
    var commodity_id: String? = null
    @SerializedName("commodity__name")
    var commodity__name: String? = null
}