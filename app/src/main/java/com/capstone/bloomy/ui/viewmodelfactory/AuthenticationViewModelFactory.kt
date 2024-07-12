package com.capstone.bloomy.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.AuthenticationInjection
import com.capstone.bloomy.repository.AuthenticationRepository
import com.capstone.bloomy.ui.viewmodel.AuthenticationViewModel

class AuthenticationViewModelFactory private constructor(private val authenticationRepository: AuthenticationRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel(authenticationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationViewModelFactory? = null

        fun getInstance(): AuthenticationViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthenticationViewModelFactory(AuthenticationInjection.provideRepository())
            }.also { INSTANCE = it }
    }
}