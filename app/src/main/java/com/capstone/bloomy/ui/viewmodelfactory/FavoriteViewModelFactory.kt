package com.capstone.bloomy.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bloomy.di.FavoriteInjection
import com.capstone.bloomy.repository.FavoriteRepository
import com.capstone.bloomy.ui.viewmodel.FavoriteViewModel

class FavoriteViewModelFactory private constructor(private val favoriteRepository: FavoriteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModelFactory? = null

        fun getInstance(context: Context): FavoriteViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteViewModelFactory(FavoriteInjection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}