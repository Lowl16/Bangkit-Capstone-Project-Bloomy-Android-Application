package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.FishPricingInjection
import com.capstone.bloomy.repository.FishPricingRepository
import com.capstone.bloomy.ui.viewmodel.FishPricingViewModel

class FishPricingViewModelFactory private constructor(private val fishPricingRepository: FishPricingRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FishPricingViewModel::class.java)) {
            return FishPricingViewModel(fishPricingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FishPricingViewModelFactory? = null

        fun getInstance(context: Context): FishPricingViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FishPricingViewModelFactory(FishPricingInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}