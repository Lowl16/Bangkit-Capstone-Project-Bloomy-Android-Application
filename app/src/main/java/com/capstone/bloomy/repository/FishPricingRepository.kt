package com.capstone.bloomy.repository

import androidx.lifecycle.liveData
import com.capstone.bloomy.data.remote.fishpricing.FishPricingService
import com.capstone.bloomy.data.response.FishPricingResponse
import com.capstone.bloomy.data.state.ResultState
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class FishPricingRepository private constructor(private val fishPricingService: FishPricingService) {

    fun fishPricing(grade: Float, catchingMethod: Float, sustainability: Float, actualPrice: Float) = liveData {
        emit(ResultState.Loading)

        val requestGrade = grade.toString().toRequestBody("text/plain".toMediaType())
        val requestCatchingMethod = catchingMethod.toString().toRequestBody("text/plain".toMediaType())
        val requestSustainability = sustainability.toString().toRequestBody("text/plain".toMediaType())
        val requestActualPrice = actualPrice.toString().toRequestBody("text/plain".toMediaType())

        try {
            val successResponse = fishPricingService.fishPricing(requestGrade, requestCatchingMethod, requestSustainability, requestActualPrice)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FishPricingResponse::class.java)

            emit(ResultState.Error(errorResponse.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FishPricingRepository? = null

        fun getInstance(
            fishPricingService: FishPricingService,
        ): FishPricingRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FishPricingRepository(fishPricingService)
            }.also { INSTANCE = it }
    }
}