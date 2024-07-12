package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.ProfileInjection
import com.capstone.bloomy.repository.ProfileRepository
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel

class ProfileViewModelFactory private constructor(private val profileRepository: ProfileRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null

        fun getInstance(context: Context): ProfileViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileViewModelFactory(ProfileInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}