package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.location.LocationConfig
import com.capstone.bloomy.repository.LocationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object LocationInjection {

    fun provideRepository(context: Context): LocationRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val locationService = LocationConfig.getApiService(user.token)

        return LocationRepository.getInstance(locationService)
    }
}