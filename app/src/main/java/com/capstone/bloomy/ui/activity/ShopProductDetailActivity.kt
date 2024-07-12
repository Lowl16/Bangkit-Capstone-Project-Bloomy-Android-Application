package com.capstone.bloomy.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProductByIdData
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.ActivityShopProductDetailBinding
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory

class ShopProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopProductDetailBinding

    private val productId = PRODUCT_ID
    private val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@ShopProductDetailActivity)
    private val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarShopProductDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        productViewModel.getProductById(productId)
        productViewModel.detailProduct.observe(this) { detailProduct ->
            setDetailProduct(detailProduct)
        }

        binding.btnEditProductDetail.setOnClickListener {
            val shopEditProductIntent = Intent(this, ShopEditProductActivity::class.java)
            shopEditProductIntent.putExtra("product_id", productId)
            startActivity(shopEditProductIntent)
        }

        binding.btnRemoveProductDetail.setOnClickListener {
            showRemoveProductDialog()
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToShopActivity()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToShopActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showRemoveProductDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.remove_product_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnNoRemoveProduct: Button = dialog.findViewById(R.id.btn_no_remove_product_dialog)
        val btnYesRemoveProduct: Button = dialog.findViewById(R.id.btn_yes_remove_product_dialog)

        btnYesRemoveProduct.setOnClickListener {
            productViewModel.deleteProduct(productId).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> { }

                        is ResultState.Success -> {
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                            val shopIntent = Intent(this, ShopActivity::class.java)
                            shopIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(shopIntent)
                        }

                        is ResultState.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        btnNoRemoveProduct.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setDetailProduct(detailProduct: ProductByIdData) {
        with(binding) {
            Glide.with(this@ShopProductDetailActivity)
                .load(detailProduct.picture)
                .into(imgShopProductDetail)

            tvTitleShopProductDetail.text = detailProduct.nama
            tvGradeShopProductDetail.text = formatGrade(detailProduct.grade)
            tvPriceShopProductDetail.text = formatCurrency(detailProduct.price) + "/kg"
            tvQuantityShopProductDetail.text = formatWeight(detailProduct.weight)
            tvValueDescriptionShopProductDetail.text = detailProduct.description
        }
    }

    private fun formatGrade(grade: String): String {
        return "Grade \"$grade\""
    }

    private fun formatCurrency(amount: Int): String {
        val formattedAmount = String.format("Rp%,d", amount)
        return formattedAmount.replace(',', '.')
    }

    private fun formatWeight(weight: Int): String {
        return "$weight kg left"
    }

    private fun navigateToShopActivity() {
        val intent = Intent(this, ShopActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        var PRODUCT_ID = "product_id"
    }
}