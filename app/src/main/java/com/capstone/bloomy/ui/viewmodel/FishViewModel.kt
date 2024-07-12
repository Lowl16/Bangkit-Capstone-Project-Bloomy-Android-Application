package com.capstone.bloomy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.FishData
import com.capstone.bloomy.repository.FishRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FishViewModel(private val fishRepository: FishRepository) : ViewModel() {

    private val _fish = MutableLiveData<List<FishData>>()
    val fish: LiveData<List<FishData>> = _fish

    private val _responseCode = MutableLiveData<Int>()

    fun getFish() {
        viewModelScope.launch {
            try {
                val response = fishRepository.getFish()

                if (response.isSuccessful) _fish.value = response.body()?.fishData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }
}