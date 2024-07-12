package com.capstone.bloomy.data.remote.product

import com.capstone.bloomy.data.response.AddProductResponse
import com.capstone.bloomy.data.response.DeleteProductResponse
import com.capstone.bloomy.data.response.EditPhotoProductResponse
import com.capstone.bloomy.data.response.EditProductResponse
import com.capstone.bloomy.data.response.ProductByGradeResponse
import com.capstone.bloomy.data.response.ProductByIdResponse
import com.capstone.bloomy.data.response.ProductByNameResponse
import com.capstone.bloomy.data.response.ProductByUsernameResponse
import com.capstone.bloomy.data.response.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun getProduct(): Response<ProductResponse>

    @GET("product")
    suspend fun getProductByUsername(
        @Query("username") username: String
    ): Response<ProductByUsernameResponse>

    @GET("product")
    suspend fun getProductById(
        @Query("id") id: String
    ): Response<ProductByIdResponse>

    @GET("product")
    suspend fun getProductByName(
        @Query("nama") nama: String
    ): Response<ProductByNameResponse>

    @GET("product")
    suspend fun getProductByGrade(
        @Query("grade") grade: String
    ): Response<ProductByGradeResponse>

    @Multipart
    @POST("product")
    suspend fun addProduct(
        @Part file: MultipartBody.Part,
        @Part("nama") nama: RequestBody,
        @Part("grade") grade: RequestBody,
        @Part("price") price: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("description") description: RequestBody
    ): AddProductResponse

    @FormUrlEncoded
    @PUT("product")
    suspend fun editProduct(
        @Query("id") id: String,
        @Field("nama") nama: String,
        @Field("grade") grade: String,
        @Field("price") price: Number,
        @Field("weight") weight: Number,
        @Field("description") description: String
    ): EditProductResponse

    @Multipart
    @PUT("product/photo")
    suspend fun editPhotoProduct(
        @Query("id") id: String,
        @Part file: MultipartBody.Part
    ): EditPhotoProductResponse

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String
    ): DeleteProductResponse
}