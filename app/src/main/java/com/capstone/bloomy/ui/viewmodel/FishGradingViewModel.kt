package com.capstone.bloomy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.capstone.bloomy.repository.FishGradingRepository
import java.io.File

class FishGradingViewModel(private val fishGradingRepository: FishGradingRepository) : ViewModel() {

    fun fishGrading(file: File) = fishGradingRepository.fishGrading(file)
}