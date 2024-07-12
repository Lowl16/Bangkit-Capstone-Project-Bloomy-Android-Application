package com.capstone.bloomy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.KotaData
import com.capstone.bloomy.data.response.ProvinsiData
import com.capstone.bloomy.repository.LocationRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    private val _provinsiData = MutableLiveData<List<ProvinsiData>>()
    val provinsiData: LiveData<List<ProvinsiData>> = _provinsiData

    private val _kotaData = MutableLiveData<List<KotaData>>()
    val kotaData: LiveData<List<KotaData>> = _kotaData

    private val _responseCode = MutableLiveData<Int>()

    fun getProvinsi() {
        viewModelScope.launch {
            try {
                val response = locationRepository.getProvinsi()

                if (response.isSuccessful) _provinsiData.value = response.body()?.provinsiData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getKota(id: Int) {
        viewModelScope.launch {
            try {
                val response = locationRepository.getKota(id)

                if (response.isSuccessful) _kotaData.value = response.body()?.kotaData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }
}