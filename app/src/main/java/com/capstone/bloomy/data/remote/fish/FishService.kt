package com.capstone.bloomy.data.remote.fish

import com.capstone.bloomy.data.response.FishResponse
import retrofit2.Response
import retrofit2.http.GET

interface FishService {

    @GET("fishs")
    suspend fun getFish(): Response<FishResponse>
}