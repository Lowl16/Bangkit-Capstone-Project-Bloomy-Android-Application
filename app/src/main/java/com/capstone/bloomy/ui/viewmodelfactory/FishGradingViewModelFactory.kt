package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.FishGradingInjection
import com.capstone.bloomy.repository.FishGradingRepository
import com.capstone.bloomy.ui.viewmodel.FishGradingViewModel

class FishGradingViewModelFactory private constructor(private val fishGradingRepository: FishGradingRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FishGradingViewModel::class.java)) {
            return FishGradingViewModel(fishGradingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FishGradingViewModelFactory? = null

        fun getInstance(context: Context): FishGradingViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FishGradingViewModelFactory(FishGradingInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}