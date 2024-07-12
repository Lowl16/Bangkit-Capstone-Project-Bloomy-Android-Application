package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class ProfileByUsernameResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val profileByUsernameData: ProfileByUsernameData
)

data class ProfileByUsernameData(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("activated")
    val activated: Boolean,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("nohp")
    val nohp: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("provinsi")
    val provinsi: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("photo")
    val photo: String,

    @field:SerializedName("description")
    val description: String
)

data class ProfileResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val profileData: ProfileData
)

data class ProfileData(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("activated")
    val activated: Boolean,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("nohp")
    val nohp: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("provinsi")
    val provinsi: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("photo")
    val photo: String,

    @field:SerializedName("description")
    val description: String
)

data class EditProfileResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val editProfileData: EditProfileData
)

data class EditProfileData(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("actived")
    val actived: Boolean,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("provinsi")
    val provinsi: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("description")
    val description: String
)

data class EditPhotoProfileResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val editPhotoProfileData: EditPhotoProfileData
)

data class EditPhotoProfileData(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("photo")
    val photo: String
)

data class ResetPasswordResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val resetPasswordData: ResetPasswordData
)

data class ResetPasswordData(

    @field:SerializedName("username")
    val username: String
)