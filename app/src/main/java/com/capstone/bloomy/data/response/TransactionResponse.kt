package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class TransactionByIdResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val transactionByIdData: TransactionByIdData
)

data class TransactionByIdData(

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("noResi")
    val noResi: String,

    @field:SerializedName("ongkir")
    val ongkir: Int,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("datePickup")
    val datePickup: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,

    @field:SerializedName("product")
    val productTransactionByIdData: ProductTransactionByIdData,

    @field:SerializedName("buyer")
    val buyerTransactionByIdData: BuyerTransactionByIdData,

    @field:SerializedName("seller")
    val sellerTransactionByIdData: SellerTransactionByIdData
)

data class ProductTransactionByIdData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("pricePerKg")
    val pricePerKg: String
)

data class BuyerTransactionByIdData(

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String,

    @field:SerializedName("namaBuyer")
    val namaBuyer: String,

    @field:SerializedName("picture")
    val picture: String
)

data class SellerTransactionByIdData(

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("picture")
    val picture: String
)

data class PurchasesTransactionResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val purchasesTransactionData: List<PurchasesTransactionData>
)

data class PurchasesTransactionData(

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("noResi")
    val noResi: String,

    @field:SerializedName("ongkir")
    val ongkir: Int,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("datePickup")
    val datePickup: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,

    @field:SerializedName("product")
    val productPurchaseData: ProductPurchaseData,

    @field:SerializedName("seller")
    val sellerData: SellerData
)

data class ProductPurchaseData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("pricePerKg")
    val pricePerKg: String
)

data class SellerData(

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("namaSeller")
    val nameSeller: String,

    @field:SerializedName("picture")
    val picture: String
)

data class SalesTransactionResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val salesTransactionData: List<SalesTransactionData>
)

data class SalesTransactionData(

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("noResi")
    val noResi: String,

    @field:SerializedName("ongkir")
    val ongkir: Int,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("datePickup")
    val datePickup: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,

    @field:SerializedName("product")
    val productSaleData: ProductSaleData,

    @field:SerializedName("buyer")
    val buyerData: BuyerData
)

data class ProductSaleData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("pricePerKg")
    val pricePerKg: String
)

data class BuyerData(

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String,

    @field:SerializedName("namaBuyer")
    val namaBuyer: String,

    @field:SerializedName("pictureBuyer")
    val pictureBuyer: String
)

data class BuyProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val buyProductData: BuyProductData
)

data class BuyProductData(

    @field:SerializedName("noResi")
    val noResi: String,

    @field:SerializedName("ongkir")
    val ongkir: Int,

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("datePickup")
    val datePickup: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class EditPurchasesTransactionResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val editPurchasesTransactionData: EditPurchasesTransactionData
)

data class EditPurchasesTransactionData(

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("noResi")
    val noResi: String,

    @field:SerializedName("ongkir")
    val ongkir: Int,

    @field:SerializedName("datePickup")
    val datePickup: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class EditSalesTransactionResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val editSalesTransactionData: EditSalesTransactionData
)

data class EditSalesTransactionData(

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameBuyer")
    val usernameBuyer: String,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("noResi")
    val noResi: String,

    @field:SerializedName("ongkir")
    val ongkir: Int,

    @field:SerializedName("datePickup")
    val datePickup: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class DeleteTransactionResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val deleteTransactionData: DeleteTransactionData
)

data class DeleteTransactionData(

    @field:SerializedName("idTransaction")
    val idTransaction: String,

    @field:SerializedName("isDelete")
    val isDelete: Boolean
)