package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.fish.FishConfig
import com.capstone.bloomy.repository.FishRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object FishInjection {

    fun provideRepository(context: Context): FishRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val fishService = FishConfig.getApiService(user.token)

        return FishRepository.getInstance(fishService)
    }
}