package com.capstone.bloomy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.EditProductResponse
import com.capstone.bloomy.data.response.ProductByGradeData
import com.capstone.bloomy.data.response.ProductByIdData
import com.capstone.bloomy.data.response.ProductByNameData
import com.capstone.bloomy.data.response.ProductByUsernameData
import com.capstone.bloomy.data.response.ProductData
import com.capstone.bloomy.repository.ProductRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<ProductData>>()
    val products: LiveData<List<ProductData>> = _products

    private val _product = MutableLiveData<List<ProductByUsernameData>>()
    val product: LiveData<List<ProductByUsernameData>> = _product

    private val _detailProduct = MutableLiveData<ProductByIdData>()
    val detailProduct: LiveData<ProductByIdData> = _detailProduct

    private val _productByName = MutableLiveData<List<ProductByNameData>>()
    val productByName: LiveData<List<ProductByNameData>> = _productByName

    private val _productByGrade = MutableLiveData<List<ProductByGradeData>>()
    val productByGrade: LiveData<List<ProductByGradeData>> = _productByGrade

    private val _editProductResponse = MutableLiveData<EditProductResponse?>()
    val editProductResponse: LiveData<EditProductResponse?> = _editProductResponse

    private val _responseCode = MutableLiveData<Int>()

    fun getProduct() {
        viewModelScope.launch {
            try {
                val response = productRepository.getProduct()

                if (response.isSuccessful) _products.value = response.body()?.productData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getProductByUsername(username: String) {
        viewModelScope.launch {
            try {
                val response = productRepository.getProductByUsername(username)

                if (response.isSuccessful) _product.value = response.body()?.productByUsernameData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getProductById(id: String) {
        viewModelScope.launch {
            try {
                val response = productRepository.getProductById(id)

                if (response.isSuccessful) _detailProduct.value = response.body()?.productByIdData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getProductByName(nama: String) {
        viewModelScope.launch {
            try {
                val response = productRepository.getProductByName(nama)

                if (response.isSuccessful) _productByName.value = response.body()?.productByNameData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getProductByGrade(grade: String) {
        viewModelScope.launch {
            try {
                val response = productRepository.getProductByGrade(grade)

                if (response.isSuccessful) _productByGrade.value = response.body()?.productByGradeData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun addProduct(file: File, nama: String, grade: String, price: Number, weight: Number, description: String) = productRepository.addProduct(file, nama, grade, price, weight, description)

    fun defaultEditProduct() {
        _editProductResponse.value = null
    }

    fun editProduct(id: String, nama: String, grade: String, price: Number, weight: Number, description: String) {
        viewModelScope.launch {
            try {
                val message = productRepository.editProduct(id, nama, grade, price, weight, description)
                _editProductResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, EditProductResponse::class.java)
                _editProductResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun editPhotoProduct(id: String, file: File) = productRepository.editPhotoProduct(id, file)

    fun deleteProduct(id: String) = productRepository.deleteProduct(id)

    companion object {
        private val TAG = ProductViewModel::class.java.simpleName
    }
}