package com.capstone.bloomy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.capstone.bloomy.repository.FishGradingRepository
import com.capstone.bloomy.repository.FishPricingRepository
import java.io.File

class FishPricingViewModel(private val fishPricingRepository: FishPricingRepository) : ViewModel() {

    fun fishPricing(grade: Float, catchingMethod: Float, sustainability: Float, actualPrice: Float) = fishPricingRepository.fishPricing(grade, catchingMethod, sustainability, actualPrice)
}