package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.SailDecisionInjection
import com.capstone.bloomy.repository.SailDecisionRepository
import com.capstone.bloomy.ui.viewmodel.SailDecisionViewModel

class SailDecisionViewModelFactory private constructor(private val sailDecisionRepository: SailDecisionRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SailDecisionViewModel::class.java)) {
            return SailDecisionViewModel(sailDecisionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: SailDecisionViewModelFactory? = null

        fun getInstance(context: Context): SailDecisionViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SailDecisionViewModelFactory(SailDecisionInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}