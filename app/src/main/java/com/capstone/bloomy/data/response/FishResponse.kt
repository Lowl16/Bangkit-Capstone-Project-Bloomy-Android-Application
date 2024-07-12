package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class FishResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val fishData: List<FishData>
)

data class FishData(

    @field:SerializedName("idFish")
    val idFish: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)