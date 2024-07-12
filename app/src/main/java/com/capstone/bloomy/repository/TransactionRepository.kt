package com.capstone.bloomy.repository

import androidx.lifecycle.liveData
import com.capstone.bloomy.data.remote.transaction.TransactionService
import com.capstone.bloomy.data.response.DeleteTransactionResponse
import com.capstone.bloomy.data.state.ResultState
import com.google.gson.Gson
import retrofit2.HttpException

class TransactionRepository private constructor(private val transactionService: TransactionService) {

    suspend fun getTransactionById(id: String) = transactionService.getTransactionById(id)

    suspend fun getPurchasesTransaction() = transactionService.getPurchasesTransaction()

    suspend fun getSalesTransaction() = transactionService.getSalesTransaction()

    suspend fun buyProduct(idProduct: String, type: String, weight: Int, datePickup: String) = transactionService.buyProduct(idProduct, type, weight, datePickup)

    suspend fun editPurchasesTransaction(id: String, type: String, weight: Int, datePickup: String) = transactionService.editPurchasesTransaction(id, type, weight, datePickup)

    suspend fun editSalesTransaction(id: String, status: String, noResi: String, ongkir: Int) = transactionService.editSalesTransaction(id, status, noResi, ongkir)

    fun deleteTransaction(id: String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = transactionService.deleteTransaction(id)

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DeleteTransactionResponse::class.java)

            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null

        fun getInstance(
            transactionService: TransactionService
        ): TransactionRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TransactionRepository(transactionService)
            }.also { INSTANCE = it }
    }
}