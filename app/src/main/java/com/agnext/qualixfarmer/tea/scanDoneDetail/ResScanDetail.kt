package com.agnext.qualixfarmer.tea.scanDoneDetail
import com.google.gson.annotations.SerializedName


class ResScanDetail {
    @SerializedName("success")
    var success: String? = null
    @SerializedName("message")
     var message: String? = null
    @SerializedName("devMessage")
      var devMessage: String? = null
    @SerializedName("count")
       var count: String? = null
    @SerializedName("timestamp")
      var timestamp: String? = null
    @SerializedName("httpCode")
      var httpCode: String? = null
    @SerializedName("data")
      var data: ArrayList<ResScanDetailData>? = null

    class  ResScanDetailData
    {

        @SerializedName("farmerCollectedId")
         var farmerCollectedId: String? = null
        @SerializedName("createdOn")
          var createdOn: String? = null
        @SerializedName("sectionId")
         var sectionId: String? = null
        @SerializedName("sectionName")
         var sectionName: String? = null
        @SerializedName("weight")
         var weight: String? = null
        @SerializedName("coveredArea")
          var coveredArea: String? = null
        @SerializedName("farmerId")
            var farmerId: String? = null
        @SerializedName("companyId")
         var companyId: String? = null
        @SerializedName("companyName")
         var companyName: String? = null

    }

}

