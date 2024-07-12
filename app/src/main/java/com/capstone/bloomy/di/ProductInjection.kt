package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.product.ProductConfig
import com.capstone.bloomy.repository.ProductRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object ProductInjection {

    fun provideRepository(context: Context): ProductRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val productService = ProductConfig.getApiService(user.token)

        return ProductRepository.getInstance(productService)
    }
}