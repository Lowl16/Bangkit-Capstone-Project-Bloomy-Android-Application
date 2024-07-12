package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class FishGradingResponse(

    @field:SerializedName("status")
    val fishGradingStatus: FishGradingStatus
)

data class FishGradingStatus(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val fishGradingData: FishGradingData
)

data class FishGradingData(

    @field:SerializedName("class")
    val fishClass: String,

    @field:SerializedName("grade")
    val fishGrade: String
)
