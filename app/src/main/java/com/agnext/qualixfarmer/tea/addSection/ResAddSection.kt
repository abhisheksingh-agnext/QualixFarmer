package com.agnext.qualixfarmer.tea.addSection

import com.google.gson.annotations.SerializedName

class ResAddSection {
    @SerializedName("sectionVO")
    var resAddSectionData: ArrayList<ResAddSectionData>? = null


    class ResAddSectionData {
        @SerializedName("sectionId")
        var sectionId: String? = null
        @SerializedName("name")
        var name: String? = null
        @SerializedName("location")
        var location: String? = null
        @SerializedName("divisionId")
        var divisionId: String? = null
        @SerializedName("divisionName")
        var divisionName: String? = null
        @SerializedName("gardenName")
        var gardenName: String? = null
        @SerializedName("totalArea")
        var totalArea: String? = null
        @SerializedName("sectionIndicsVO")
        var sectionIndicsVO: ArrayList<SectionIndicsVO>? = null
    }

    class SectionIndicsVO
    {
        @SerializedName("sectionIndicsId")
        private var sectionIndicsId: Int? = null
        @SerializedName("latitude")
        private val latitude: String? = null
        @SerializedName("longitude")
        private val longitude: String? = null
    }
}