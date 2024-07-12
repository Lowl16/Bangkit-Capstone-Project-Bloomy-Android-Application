package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.FishInjection
import com.capstone.bloomy.repository.FishRepository
import com.capstone.bloomy.ui.viewmodel.FishViewModel

class FishViewModelFactory private constructor(private val fishRepository: FishRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FishViewModel::class.java)) {
            return FishViewModel(fishRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FishViewModelFactory? = null

        fun getInstance(context: Context): FishViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FishViewModelFactory(FishInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}