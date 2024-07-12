package com.capstone.bloomy.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.ui.viewmodel.UserPreferencesViewModel

class UserPreferencesViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            return UserPreferencesViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesViewModelFactory? = null

        fun getInstance(userPreferences: UserPreferences): UserPreferencesViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferencesViewModelFactory(userPreferences)
            }.also { INSTANCE = it }
    }
}