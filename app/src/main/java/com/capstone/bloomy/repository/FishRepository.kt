package com.capstone.bloomy.repository

import com.capstone.bloomy.data.remote.fish.FishService

class FishRepository private constructor(private val fishService: FishService) {

    suspend fun getFish() = fishService.getFish()

    companion object {
        @Volatile
        private var INSTANCE: FishRepository? = null

        fun getInstance(
            fishService: FishService
        ): FishRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FishRepository(fishService)
            }.also { INSTANCE = it }
    }
}