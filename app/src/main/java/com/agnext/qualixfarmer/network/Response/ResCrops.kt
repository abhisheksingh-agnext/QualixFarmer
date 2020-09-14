package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResCrops {
    @SerializedName("commodity_id")
    var commodity_id: String? = null
    @SerializedName("commodity_code")
    var commodity_code: String? = null
    @SerializedName("commodity_name")
    var commodity_name: String? = null
    @SerializedName("commodity_category_id")
    var commodity_category_id: String? = null
    @SerializedName("commodity_category_name")
    var commodity_category_name: String? = null
    @SerializedName("count")
    var count: String? = null
}

