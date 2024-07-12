package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.ProductInjection
import com.capstone.bloomy.repository.ProductRepository
import com.capstone.bloomy.ui.viewmodel.ProductViewModel

class ProductViewModelFactory private constructor(private val productRepository: ProductRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ProductViewModelFactory? = null

        fun getInstance(context: Context): ProductViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductViewModelFactory(ProductInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}