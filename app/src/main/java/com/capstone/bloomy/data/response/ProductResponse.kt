package com.capstone.bloomy.data.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val productData: List<ProductData>
)

data class ProductData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class ProductByUsernameResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val productByUsernameData: List<ProductByUsernameData>
)

data class ProductByUsernameData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class ProductByIdResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val productByIdData: ProductByIdData
)

data class ProductByIdData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class ProductByNameResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val productByNameData: List<ProductByNameData>
)

data class ProductByNameData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class ProductByGradeResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val productByGradeData: List<ProductByGradeData>
)

data class ProductByGradeData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class AddProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val addProductData: AddProductData
)

data class AddProductData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int
)

data class EditProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val editProductData: EditProductData
)

data class EditProductData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class EditPhotoProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val editPhotoProductData: EditPhotoProductData
)

data class EditPhotoProductData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("usernameSeller")
    val usernameSeller: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("grade")
    val grade: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class DeleteProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val deleteProductData: DeleteProductData
)

data class DeleteProductData(

    @field:SerializedName("idProduct")
    val idProduct: String,

    @field:SerializedName("isDelete")
    val isDelete: Boolean
)
