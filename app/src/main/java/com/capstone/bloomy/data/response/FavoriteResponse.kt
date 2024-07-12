package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val favoriteData: List<FavoriteData>
)

data class FavoriteData(

    @field:SerializedName("idFavorite")
    val idFavorite: String,

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class AddFavoriteResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val addFavoriteData: AddFavoriteData
)

data class AddFavoriteData(

    @field:SerializedName("idFavorite")
    val idFavorite: String,

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String
)

data class DeleteFavoriteResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val deleteFavoriteData: DeleteFavoriteData
)

data class DeleteFavoriteData(

    @field:SerializedName("idFavorite")
    val idFavorite: String,

    @field:SerializedName("isDelete")
    val isDelete: Boolean
)