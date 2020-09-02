package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResSpecxChemicalScans {

    @SerializedName("status")
    var status: Boolean? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("data")
    var data: ArrayList<ChemicalScan>? = null

    class ChemicalScan {

        @SerializedName("id")
        var id: String? = null
        @SerializedName("batchId")
        var batchId: String? = null
        @SerializedName("createdOn")
        var createdOn: String? = null
        @SerializedName("cropName")
        var cropName: String? = null
        @SerializedName("time")
        var time: String? = null
        @SerializedName("analysesList")
        var analysesList: ArrayList<AnalysesList>? = null
    }

    class AnalysesList {
        @SerializedName("amountUnit")
        var amountUnit: String? = null
        @SerializedName("analysisId")
        var analysisId: String? = null
        @SerializedName("totalAmount")
        var totalAmount: String? = null
        @SerializedName("analysisName")
        var analysisName: String? = null
        @SerializedName("result")
        var result: String? = null
        @SerializedName("avgResult")
        var avgResult: String? = null
    }
}