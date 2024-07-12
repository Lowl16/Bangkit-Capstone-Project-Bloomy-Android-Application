package com.capstone.bloomy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bloomy.data.response.BuyProductResponse
import com.capstone.bloomy.data.response.EditPurchasesTransactionResponse
import com.capstone.bloomy.data.response.EditSalesTransactionResponse
import com.capstone.bloomy.data.response.PurchasesTransactionData
import com.capstone.bloomy.data.response.SalesTransactionData
import com.capstone.bloomy.data.response.TransactionByIdData
import com.capstone.bloomy.repository.TransactionRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {

    private val _detailTransaction = MutableLiveData<TransactionByIdData>()
    val detailTransaction: LiveData<TransactionByIdData> = _detailTransaction

    private val _purchasesTransaction = MutableLiveData<List<PurchasesTransactionData>>()
    val purchasesTransaction: LiveData<List<PurchasesTransactionData>> = _purchasesTransaction

    private val _salesTransaction = MutableLiveData<List<SalesTransactionData>>()
    val salesTransaction: LiveData<List<SalesTransactionData>> = _salesTransaction

    private val _buyProductResponse = MutableLiveData<BuyProductResponse?>()
    val buyProductResponse: LiveData<BuyProductResponse?> = _buyProductResponse

    private val _editPurchasesTransactionResponse = MutableLiveData<EditPurchasesTransactionResponse?>()
    val editPurchasesTransactionResponse: LiveData<EditPurchasesTransactionResponse?> = _editPurchasesTransactionResponse

    private val _editSalesTransactionResponse = MutableLiveData<EditSalesTransactionResponse?>()
    val editSalesTransactionResponse: LiveData<EditSalesTransactionResponse?> = _editSalesTransactionResponse

    private val _responseCode = MutableLiveData<Int>()

    fun getTransactionById(id: String) {
        viewModelScope.launch {
            try {
                val response = transactionRepository.getTransactionById(id)

                if (response.isSuccessful) _detailTransaction.value = response.body()?.transactionByIdData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getPurchasesTransaction() {
        viewModelScope.launch {
            try {
                val response = transactionRepository.getPurchasesTransaction()

                if (response.isSuccessful) _purchasesTransaction.value = response.body()?.purchasesTransactionData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun getSalesTransaction() {
        viewModelScope.launch {
            try {
                val response = transactionRepository.getSalesTransaction()

                if (response.isSuccessful) _salesTransaction.value = response.body()?.salesTransactionData else _responseCode.value = response.code()
            } catch (e: HttpException) {
                _responseCode.value = e.code()
            }
        }
    }

    fun defaultBuyProduct() {
        _buyProductResponse.value = null
    }

    fun buyProduct(idProduct: String, type: String, weight: Int, datePickup: String) {
        viewModelScope.launch {
            try {
                val message = transactionRepository.buyProduct(idProduct, type, weight, datePickup)
                _buyProductResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, BuyProductResponse::class.java)
                _buyProductResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun defaultEditPurchasesTransaction() {
        _editPurchasesTransactionResponse.value = null
    }

    fun editPurchasesTransaction(id: String, type: String, weight: Int, datePickup: String) {
        viewModelScope.launch {
            try {
                val message = transactionRepository.editPurchasesTransaction(id, type, weight, datePickup)
                _editPurchasesTransactionResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, EditPurchasesTransactionResponse::class.java)
                _editPurchasesTransactionResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun defaultEditSalesTransaction() {
        _editSalesTransactionResponse.value = null
    }

    fun editSalesTransaction(id: String, status: String, noResi: String, ongkir: Int) {
        viewModelScope.launch {
            try {
                val message = transactionRepository.editSalesTransaction(id, status, noResi, ongkir)
                _editSalesTransactionResponse.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, EditSalesTransactionResponse::class.java)
                _editSalesTransactionResponse.value = errorBody
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    fun deleteTransaction(id: String) = transactionRepository.deleteTransaction(id)

    companion object {
        private val TAG = TransactionViewModel::class.java.simpleName
    }
}