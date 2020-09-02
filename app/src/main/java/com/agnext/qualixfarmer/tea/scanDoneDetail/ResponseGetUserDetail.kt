package com.agnext.qualixfarmer.tea.scanDoneDetail

import com.google.gson.annotations.SerializedName

class ResponseGetUserDetail {  @SerializedName("success")
var success: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("data")
    var data = ArrayList<UserDetails>()

}
class UserDetails {

    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("companyId")
    var companyId: String? = null
    @SerializedName("code")
    var empCode: String? = null
    @SerializedName("baseLocation")
    var baseLocation: String? = null
    @SerializedName("gardenName")
    var gardenName: String? = null


    @SerializedName("divisionList")
    var divisionList = ArrayList<DivisionList>()

}
class DivisionList {

    @SerializedName("divisionId")
    var divisionId: Int? = null
    @SerializedName("divisionName")
    var divisionName: String? = null
    @SerializedName("sectionVO")
    var sectionList = ArrayList<SectionData>()

}

class SectionData {

    @SerializedName("sectionId")
    var sectionId: Int? = null
    @SerializedName("name")
    var name: String? = null

}