package com.capstone.bloomy.data.remote.saildecision

import com.capstone.bloomy.data.response.SailDecisionResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SailDecisionService {

    @Multipart
    @POST("sail_decision/predict")
    suspend fun sailDecision(
        @Part("outlook") outlook: Int,
        @Part("temperature") temperature: Int,
        @Part("humidity") humidity: Int,
        @Part("wind") wind: Int
    ): SailDecisionResponse
}