package com.capstone.bloomy.data.remote.currentweather

import com.capstone.bloomy.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrentWeatherConfig {

    val api: CurrentWeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrentWeatherService::class.java)
    }
}