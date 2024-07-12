package com.capstone.bloomy.repository

import androidx.lifecycle.liveData
import com.capstone.bloomy.data.remote.fishgrading.FishGradingService
import com.capstone.bloomy.data.response.FishGradingResponse
import com.capstone.bloomy.data.state.ResultState
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class FishGradingRepository private constructor(private val fishGradingService: FishGradingService) {

    fun fishGrading(file: File) = liveData {
        emit(ResultState.Loading)

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        try {
            val successResponse = fishGradingService.fishGrading(multipartBody)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FishGradingResponse::class.java)

            emit(ResultState.Error(errorResponse.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FishGradingRepository? = null

        fun getInstance(
            fishGradingService: FishGradingService,
        ): FishGradingRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FishGradingRepository(fishGradingService)
            }.also { INSTANCE = it }
    }
}