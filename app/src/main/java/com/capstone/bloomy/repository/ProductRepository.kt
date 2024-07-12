package com.capstone.bloomy.repository

import androidx.lifecycle.liveData
import com.capstone.bloomy.data.remote.product.ProductService
import com.capstone.bloomy.data.response.AddProductResponse
import com.capstone.bloomy.data.response.DeleteProductResponse
import com.capstone.bloomy.data.response.EditPhotoProductResponse
import com.capstone.bloomy.data.state.ResultState
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class ProductRepository private constructor(private val productService: ProductService) {

    suspend fun getProduct() = productService.getProduct()

    suspend fun getProductByUsername(username: String) = productService.getProductByUsername(username)

    suspend fun getProductById(id: String) = productService.getProductById(id)

    suspend fun getProductByName(name: String) = productService.getProductByName(name)

    suspend fun getProductByGrade(grade: String) = productService.getProductByGrade(grade)

    fun addProduct(file: File, nama: String, grade: String, price: Number, weight: Number, description: String) = liveData {
        emit(ResultState.Loading)

        val requestNama = nama.toRequestBody("text/plain".toMediaType())
        val requestGrade = grade.toRequestBody("text/plain".toMediaType())
        val requestPrice = price.toString().toRequestBody("text/plain".toMediaType())
        val requestWeight = weight.toString().toRequestBody("text/plain".toMediaType())
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        try {
            val successResponse = productService.addProduct(multipartBody, requestNama, requestGrade, requestPrice, requestWeight, requestDescription)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddProductResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun editProduct(id: String, nama: String, grade: String, price: Number, weight: Number, description: String) = productService.editProduct(id, nama, grade, price, weight, description)

    fun editPhotoProduct(id: String, file: File) = liveData {
        emit(ResultState.Loading)

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        try {
            val successResponse = productService.editPhotoProduct(id, multipartBody)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditPhotoProductResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun deleteProduct(id: String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = productService.deleteProduct(id)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DeleteProductResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProductRepository? = null

        fun getInstance(
            productService: ProductService
        ): ProductRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductRepository(productService)
            }.also { INSTANCE = it }
    }
}