package com.capstone.bloomy.data.remote.authentication

import com.capstone.bloomy.data.response.ForgotPasswordResponse
import com.capstone.bloomy.data.response.SendVerificationLinkResponse
import com.capstone.bloomy.data.response.SignInResponse
import com.capstone.bloomy.data.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationService {

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse

    @FormUrlEncoded
    @POST("auth/verify/send")
    suspend fun sendVerificationLink(
        @Field("email") email: String
    ): SendVerificationLinkResponse

    @FormUrlEncoded
    @POST("auth/forgot/link")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): ForgotPasswordResponse
}