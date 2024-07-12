package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class SailDecisionResponse(

    @field:SerializedName("status")
    val status: Status
)

data class Status(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val sailDecisionData: SailDecisionData,

    @field:SerializedName("message")
    val message: String
)

data class SailDecisionData(

    @field:SerializedName("class_predict")
    val classPredict: Int,

    @field:SerializedName("decision")
    val decision: String,

    @field:SerializedName("precentage")
    val precentage: Float
)