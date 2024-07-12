package com.capstone.bloomy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.SailDecisionResponse
import com.capstone.bloomy.repository.SailDecisionRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SailDecisionViewModel(private val sailDecisionRepository: SailDecisionRepository) : ViewModel() {

    private val _sailDecisionResponse = MutableLiveData<SailDecisionResponse?>()
    val sailDecisionResponse: LiveData<SailDecisionResponse?> = _sailDecisionResponse

    fun defaultSailDecision() {
        _sailDecisionResponse.value = null
    }

    fun sailDecision(outlook: Int, temperature: Int, humidity: Int, wind: Int) {
        viewModelScope.launch {
            try {
                val message = sailDecisionRepository.sailDecision(outlook, temperature, humidity, wind)
                _sailDecisionResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SailDecisionResponse::class.java)
                _sailDecisionResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    companion object {
        private val TAG = SailDecisionViewModel::class.java.simpleName
    }
}