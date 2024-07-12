package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.LocationInjection
import com.capstone.bloomy.repository.LocationRepository
import com.capstone.bloomy.ui.viewmodel.LocationViewModel

class LocationViewModelFactory private constructor(private val locationRepository: LocationRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(locationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: LocationViewModelFactory? = null

        fun getInstance(context: Context): LocationViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationViewModelFactory(LocationInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}