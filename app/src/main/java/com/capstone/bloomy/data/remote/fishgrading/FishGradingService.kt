package com.capstone.bloomy.data.remote.fishgrading

import com.capstone.bloomy.data.response.FishGradingResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FishGradingService {

    @Multipart
    @POST("marine/predict")
    suspend fun fishGrading(
        @Part file: MultipartBody.Part
    ): FishGradingResponse
}