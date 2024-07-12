package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class ProvinsiResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val provinsiData: List<ProvinsiData>
)

data class ProvinsiData(

    @field:SerializedName("id")
    val id: Int,

    @field: SerializedName("nama")
    val nama: String
)

data class KotaResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val kotaData: List<KotaData>
)

data class KotaData(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("id_provinsi")
    val idProvinsi: Int,

    @field:SerializedName("nama")
    val nama: String
)