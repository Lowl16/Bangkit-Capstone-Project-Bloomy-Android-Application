package com.capstone.bloomy.data.remote.location

import com.capstone.bloomy.data.response.KotaResponse
import com.capstone.bloomy.data.response.ProvinsiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationService {

    @GET("location/provinsi")
    suspend fun getProvinsi(): Response<ProvinsiResponse>

    @GET("location/provinsi/{id}/kota")
    suspend fun getKota(
        @Path("id") id: Int
    ): Response<KotaResponse>
}