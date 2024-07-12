package com.capstone.bloomy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.EditProfileResponse
import com.capstone.bloomy.data.response.ProfileByUsernameData
import com.capstone.bloomy.data.response.ProfileData
import com.capstone.bloomy.data.response.ResetPasswordResponse
import com.capstone.bloomy.repository.ProfileRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profileByUsername = MutableLiveData<ProfileByUsernameData>()
    val profileByUsername: LiveData<ProfileByUsernameData> = _profileByUsername

    private val _profile = MutableLiveData<ProfileData>()
    val profile: LiveData<ProfileData> = _profile

    private val _editProfileResponse = MutableLiveData<EditProfileResponse?>()
    val editProfileResponse: LiveData<EditProfileResponse?> = _editProfileResponse

    private val _resetPasswordResponse = MutableLiveData<ResetPasswordResponse?>()
    val resetPasswordResponse: LiveData<ResetPasswordResponse?> = _resetPasswordResponse

    private val _responseCode = MutableLiveData<Int>()

    fun getProfileByUsername(username: String) {
        viewModelScope.launch {
            try {
                val response = profileRepository.getProfileByUsername(username)

                if (response.isSuccessful) _profileByUsername.value = response.body()?.profileByUsernameData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val response = profileRepository.getProfile()

                if (response.isSuccessful) _profile.value = response.body()?.profileData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun tokenInvalid() = profileRepository.tokenInvalid()

    fun defaultEditProfile() {
        _editProfileResponse.value = null
    }

    fun editProfile(nama: String, nohp: String, alamat: String, provinsi: String, kota: String, description: String) {
        viewModelScope.launch {
            try {
                val message = profileRepository.editProfile(nama, nohp, alamat, provinsi, kota, description)
                _editProfileResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, EditProfileResponse::class.java)
                _editProfileResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun editPhotoProfile(file: File) = profileRepository.editPhotoProfile(file)

    fun defaultResetPassword() {
        _resetPasswordResponse.value = null
    }

    fun resetPassword(oldPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            try {
                val message = profileRepository.resetPassword(oldPassword, newPassword, confirmNewPassword)
                _resetPasswordResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ResetPasswordResponse::class.java)
                _resetPasswordResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName
    }
}