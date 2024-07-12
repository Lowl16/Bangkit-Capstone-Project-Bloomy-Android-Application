package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.fishpricing.FishPricingConfig
import com.capstone.bloomy.repository.FishPricingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object FishPricingInjection {

    fun provideRepository(context: Context): FishPricingRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val apiService = FishPricingConfig.getApiService(user.token)

        return FishPricingRepository.getInstance(apiService)
    }
}