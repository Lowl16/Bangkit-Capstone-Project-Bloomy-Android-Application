package com.capstone.bloomy.repository

import androidx.lifecycle.liveData
import com.capstone.bloomy.data.remote.profile.ProfileService
import com.capstone.bloomy.data.response.EditPhotoProfileResponse
import com.capstone.bloomy.data.response.ProfileResponse
import com.capstone.bloomy.data.state.ResultState
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class ProfileRepository private constructor(private val profileService: ProfileService) {

    suspend fun getProfileByUsername(username: String) = profileService.getProfileByUsername(username)

    suspend fun getProfile() = profileService.getProfile()

    fun tokenInvalid() = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = profileService.tokenInvalid()

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ProfileResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun editProfile(nama: String, nohp: String, alamat: String, provinsi: String, kota: String, description: String) = profileService.editProfile(nama, nohp, alamat, provinsi, kota, description)

    fun editPhotoProfile(file: File) = liveData {
        emit(ResultState.Loading)

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        try {
            val successResponse = profileService.editPhotoProfile(multipartBody)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditPhotoProfileResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun resetPassword(oldPassword: String, newPassword: String, confirmNewPassword: String) = profileService.resetPassword(oldPassword, newPassword, confirmNewPassword)

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(
            profileService: ProfileService
        ): ProfileRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(profileService)
            }.also { INSTANCE = it }
    }
}