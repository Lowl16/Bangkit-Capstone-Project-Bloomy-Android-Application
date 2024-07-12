package com.capstone.bloomy.repository

import com.capstone.bloomy.data.remote.saildecision.SailDecisionService

class SailDecisionRepository private constructor(private val sailDecisionService: SailDecisionService) {

    suspend fun sailDecision(outlook: Int, temperature: Int, humidity: Int, wind: Int) = sailDecisionService.sailDecision(outlook, temperature, humidity, wind)

    companion object {
        @Volatile
        private var INSTANCE: SailDecisionRepository? = null

        fun getInstance(
            sailDecisionService: SailDecisionService,
        ): SailDecisionRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SailDecisionRepository(sailDecisionService)
            }.also { INSTANCE = it }
    }
}