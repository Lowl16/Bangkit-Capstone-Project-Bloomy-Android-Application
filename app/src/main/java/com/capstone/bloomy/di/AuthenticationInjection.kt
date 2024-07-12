package com.capstone.bloomy.di

import com.capstone.bloomy.data.remote.authentication.AuthenticationConfig
import com.capstone.bloomy.repository.AuthenticationRepository

object AuthenticationInjection {

    fun provideRepository(): AuthenticationRepository {
        val apiService = AuthenticationConfig.getApiService()

        return AuthenticationRepository.getInstance(apiService)
    }
}