package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.TransactionInjection
import com.capstone.bloomy.repository.TransactionRepository
import com.capstone.bloomy.ui.viewmodel.TransactionViewModel

class TransactionViewModelFactory private constructor(private val transactionRepository: TransactionRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: TransactionViewModelFactory? = null

        fun getInstance(context: Context): TransactionViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TransactionViewModelFactory(TransactionInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}