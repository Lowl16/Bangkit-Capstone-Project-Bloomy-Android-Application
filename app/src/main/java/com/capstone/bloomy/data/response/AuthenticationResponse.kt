package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val signUpData: SignUpData
)

data class SignUpData(

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("verify")
    val verify: String
)

data class SignInResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val signInData: SignInData
)

data class SignInData (

    @field:SerializedName("token")
    val token: String
)

data class SendVerificationLinkResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val sendVerificationLinkData: SendVerificationLinkData
)

data class SendVerificationLinkData(

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("verify")
    val verify: String
)

data class ForgotPasswordResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val forgotPasswordData: String
)