package com.capstone.bloomy.repository

import com.capstone.bloomy.data.remote.location.LocationService

class LocationRepository private constructor(private val locationService: LocationService) {

    suspend fun getProvinsi() = locationService.getProvinsi()

    suspend fun getKota(id: Int) = locationService.getKota(id)

    companion object {
        @Volatile
        private var INSTANCE: LocationRepository? = null

        fun getInstance(
            locationService: LocationService
        ): LocationRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationRepository(locationService)
            }.also { INSTANCE = it }
    }
}