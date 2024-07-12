package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.fishgrading.FishGradingConfig
import com.capstone.bloomy.repository.FishGradingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object FishGradingInjection {

    fun provideRepository(context: Context): FishGradingRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val apiService = FishGradingConfig.getApiService(user.token)

        return FishGradingRepository.getInstance(apiService)
    }
}