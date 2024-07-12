package com.capstone.bloomy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.model.UserModel
import com.capstone.bloomy.data.preferences.UserPreferences
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userPreferences.getSession().asLiveData()
    }

    fun saveSession(userModel: UserModel) {
        viewModelScope.launch {
            userPreferences.saveSession(userModel)
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            userPreferences.clearSession()
        }
    }
}