package com.capstone.bloomy.data.remote.transaction

import com.capstone.bloomy.data.response.BuyProductResponse
import com.capstone.bloomy.data.response.DeleteTransactionResponse
import com.capstone.bloomy.data.response.EditPurchasesTransactionResponse
import com.capstone.bloomy.data.response.EditSalesTransactionResponse
import com.capstone.bloomy.data.response.PurchasesTransactionResponse
import com.capstone.bloomy.data.response.SalesTransactionResponse
import com.capstone.bloomy.data.response.TransactionByIdResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionService {

    @GET("transaction")
    suspend fun getTransactionById(
        @Query("id") id: String
    ): Response<TransactionByIdResponse>

    @GET("transactions/me/buyer")
    suspend fun getPurchasesTransaction(): Response<PurchasesTransactionResponse>

    @GET("transactions/me/seller")
    suspend fun getSalesTransaction(): Response<SalesTransactionResponse>

    @FormUrlEncoded
    @POST("transaction")
    suspend fun buyProduct(
        @Field("idProduct") idProduct: String,
        @Field("type") type: String,
        @Field("weight") weight: Int,
        @Field("datePickup") datePickup: String
    ): BuyProductResponse

    @FormUrlEncoded
    @PUT("transaction/buyer/{id}")
    suspend fun editPurchasesTransaction(
        @Path("id") id: String,
        @Field("type") type: String,
        @Field("weight") weight: Int,
        @Field("datePickup") datePickup: String
    ): EditPurchasesTransactionResponse

    @FormUrlEncoded
    @PUT("transaction/seller/{id}")
    suspend fun editSalesTransaction(
        @Path("id") id: String,
        @Field("status") status: String,
        @Field("noResi") noResi: String,
        @Field("ongkir") ongkir: Int
    ): EditSalesTransactionResponse

    @DELETE("transaction/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: String
    ): DeleteTransactionResponse
}