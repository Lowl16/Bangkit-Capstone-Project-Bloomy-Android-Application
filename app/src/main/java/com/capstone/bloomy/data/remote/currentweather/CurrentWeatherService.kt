package com.capstone.bloomy.data.remote.currentweather

import com.capstone.bloomy.data.model.CurrentWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherService {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<CurrentWeatherModel>
}