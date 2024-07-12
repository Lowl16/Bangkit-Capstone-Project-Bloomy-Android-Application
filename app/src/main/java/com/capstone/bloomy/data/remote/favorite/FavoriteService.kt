package com.capstone.bloomy.data.remote.favorite

import com.capstone.bloomy.data.response.AddFavoriteResponse
import com.capstone.bloomy.data.response.DeleteFavoriteResponse
import com.capstone.bloomy.data.response.FavoriteResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteService {

    @GET("favorite/me")
    suspend fun getFavorite(): Response<FavoriteResponse>

    @FormUrlEncoded
    @POST("favorite")
    suspend fun addFavorite(
        @Field("idProduct") idProduct: String
    ): AddFavoriteResponse

    @DELETE("favorite/{idFavorite}")
    suspend fun deleteFavorite(
        @Path("idFavorite") idFavorite: String
    ): DeleteFavoriteResponse
}