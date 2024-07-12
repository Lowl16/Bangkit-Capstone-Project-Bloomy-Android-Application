package com.capstone.bloomy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.AddFavoriteResponse
import com.capstone.bloomy.data.response.FavoriteData
import com.capstone.bloomy.repository.FavoriteRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    private val _favorite = MutableLiveData<List<FavoriteData>>()
    val favorite: LiveData<List<FavoriteData>> = _favorite

    private val _addFavoriteResponse = MutableLiveData<AddFavoriteResponse?>()
    val addFavoriteResponse: LiveData<AddFavoriteResponse?> = _addFavoriteResponse

    private val _responseCode = MutableLiveData<Int>()

    fun getFavorite() {
        viewModelScope.launch {
            try {
                val response = favoriteRepository.getFavorite()

                if (response.isSuccessful) _favorite.value = response.body()?.favoriteData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun defaultAddFavorite() {
        _addFavoriteResponse.value = null
    }

    fun addFavorite(idProduct: String) {
        viewModelScope.launch {
            try {
                val message = favoriteRepository.addFavorite(idProduct)
                _addFavoriteResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, AddFavoriteResponse::class.java)
                _addFavoriteResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun deleteFavorite(idFavorite: String) = favoriteRepository.deleteFavorite(idFavorite)

    companion object {
        private val TAG = FavoriteViewModel::class.java.simpleName
    }
}