package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class FishPricingResponse(

    @field:SerializedName("status")
    val fishPricingStatus: FishPricingStatus
)

data class FishPricingStatus(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val fishPricingData: FishPricingData
)

data class FishPricingData(

    @field:SerializedName("price")
    val price: Int
)