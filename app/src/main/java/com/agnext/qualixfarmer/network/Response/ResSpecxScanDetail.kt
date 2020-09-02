package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResSpecxScanDetail
{

    @SerializedName("analysis_type")
    var analysis_type: String? = null

    @SerializedName("analysis_value")
    var analysis_value: String? = null

    @SerializedName("analysis_unit")
    var analysis_unit: String? = null

    @SerializedName("analysis_result")
    var analysis_result: String? = null
}