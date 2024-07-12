package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.saildecision.SailDecisionConfig
import com.capstone.bloomy.repository.SailDecisionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object SailDecisionInjection {

    fun provideRepository(context: Context): SailDecisionRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val apiService = SailDecisionConfig.getApiService(user.token)

        return SailDecisionRepository.getInstance(apiService)
    }
}