package com.agnext.qualixfarmer.network.Response

import com.google.gson.annotations.SerializedName

class ResAllFarms {

        @SerializedName("cropId")
        var cropId: String? = null
        @SerializedName("cropName")
        var cropName: String? = null
        @SerializedName("cropVerityId")
        var cropVerityId: String? = null
        @SerializedName("cropVerityName")
        var cropVerityName: String? = null
        @SerializedName("farmId")
        var farmId: String? = null
        @SerializedName("farmerId")
        var farmerId: String? = null
        @SerializedName("farmerName")
        var farmerName: String? = null
        @SerializedName("district")
        var district: String? = null
        @SerializedName("area")
        var area: String? = null
        @SerializedName("address")
        var address: String? = null
        @SerializedName("farmIndics")
        var farmIndics: ArrayList<LatLong>? = null

    }

    class LatLong {
        @SerializedName("farmIndicsId")
        var farmIndicsId: String? = null
        @SerializedName("latitude")
        var latitude: String? = null
        @SerializedName("longitude")
        var longitude: String? = null

    }
