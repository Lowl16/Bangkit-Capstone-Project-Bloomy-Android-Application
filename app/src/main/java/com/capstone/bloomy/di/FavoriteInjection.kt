package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.favorite.FavoriteConfig
import com.capstone.bloomy.repository.FavoriteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object FavoriteInjection {

    fun provideRepository(context: Context): FavoriteRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val favoriteService = FavoriteConfig.getApiService(user.token)

        return FavoriteRepository.getInstance(favoriteService)
    }
}