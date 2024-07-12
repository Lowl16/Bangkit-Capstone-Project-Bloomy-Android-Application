package com.capstone.bloomy.di

import android.content.Context
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.remote.transaction.TransactionConfig
import com.capstone.bloomy.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object TransactionInjection {

    fun provideRepository(context: Context): TransactionRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val transactionService = TransactionConfig.getApiService(user.token)

        return TransactionRepository.getInstance(transactionService)
    }
}