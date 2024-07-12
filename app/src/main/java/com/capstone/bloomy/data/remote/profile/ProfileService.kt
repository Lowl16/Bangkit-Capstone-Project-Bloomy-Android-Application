package com.capstone.bloomy.data.remote.profile

import com.capstone.bloomy.data.response.EditPhotoProfileResponse
import com.capstone.bloomy.data.response.EditProfileResponse
import com.capstone.bloomy.data.response.ProfileByUsernameResponse
import com.capstone.bloomy.data.response.ProfileResponse
import com.capstone.bloomy.data.response.ResetPasswordResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileService {

    @GET("user")
    suspend fun getProfileByUsername(
        @Query("username") username: String,
    ): Response<ProfileByUsernameResponse>

    @GET("user/me")
    suspend fun getProfile(): Response<ProfileResponse>

    @GET("user/me")
    suspend fun tokenInvalid(): ProfileResponse

    @FormUrlEncoded
    @PUT("user")
    suspend fun editProfile(
        @Field("nama") nama: String,
        @Field("nohp") nohp: String,
        @Field("alamat") alamat: String,
        @Field("provinsi") provinsi: String,
        @Field("kota") kota: String,
        @Field("description") description: String
    ): EditProfileResponse

    @Multipart
    @PUT("user/photo")
    suspend fun editPhotoProfile(
        @Part file: MultipartBody.Part
    ): EditPhotoProfileResponse

    @FormUrlEncoded
    @PUT("user/password")
    suspend fun resetPassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmNewPassword") confirmNewPassword: String,
    ): ResetPasswordResponse
}