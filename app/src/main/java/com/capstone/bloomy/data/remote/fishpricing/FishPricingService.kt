package com.capstone.bloomy.data.remote.fishpricing

import com.capstone.bloomy.data.response.FishPricingResponse
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FishPricingService {

    @Multipart
    @POST("price/predict")
    suspend fun fishPricing(
        @Part("grade") grade: RequestBody,
        @Part("catchingMethod") catchingMethod: RequestBody,
        @Part("sustainability") sustainability: RequestBody,
        @Part("actualPrice") actualPrice: RequestBody
    ): FishPricingResponse
}