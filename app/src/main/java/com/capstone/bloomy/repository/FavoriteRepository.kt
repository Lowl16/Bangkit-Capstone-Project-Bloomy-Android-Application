package com.capstone.bloomy.repository

import androidx.lifecycle.liveData
import com.capstone.bloomy.data.remote.favorite.FavoriteService
import com.capstone.bloomy.data.response.DeleteFavoriteResponse
import com.capstone.bloomy.data.state.ResultState
import com.google.gson.Gson
import retrofit2.HttpException

class FavoriteRepository private constructor(private val favoriteService: FavoriteService) {

    suspend fun getFavorite() = favoriteService.getFavorite()

    suspend fun addFavorite(idProduct: String) = favoriteService.addFavorite(idProduct)

    fun deleteFavorite(idFavorite: String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = favoriteService.deleteFavorite(idFavorite)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DeleteFavoriteResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(
            favoriteService: FavoriteService
        ): FavoriteRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteRepository(favoriteService)
            }.also { INSTANCE = it }
    }
}