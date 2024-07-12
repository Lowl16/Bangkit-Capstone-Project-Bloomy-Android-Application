package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.profile.ProfileConfig
import com.capstone.bloomy.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object ProfileInjection {

    fun provideRepository(context: Context): ProfileRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val profileService = ProfileConfig.getApiService(user.token)

        return ProfileRepository.getInstance(profileService)
    }
}