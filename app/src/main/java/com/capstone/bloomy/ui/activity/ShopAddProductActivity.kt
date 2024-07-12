package com.capstone.bloomy.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.capstone.bloomy.R
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.ActivityShopAddProductBinding
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory
import com.capstone.bloomy.utils.getImageUri
import com.capstone.bloomy.utils.reduceFileImage
import com.capstone.bloomy.utils.uriToFile
import com.google.android.material.button.MaterialButton

class ShopAddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopAddProductBinding

    private var imageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarShopAddProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@ShopAddProductActivity)
        val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

        binding.cardViewImageAddProduct.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnAddProduct.setOnClickListener {
            imageUri?.let { uri ->
                val addProduct = binding.btnAddProduct
                val file = uriToFile(uri, this).reduceFileImage()
                val nama = binding.etTitleAddProduct.text.toString()
                val grade = binding.etGradeAddProduct.text.toString()
                val priceString = binding.etPriceKgAddProduct.text.toString()
                val weightString = binding.etQuantityKgAddProduct.text.toString()
                val description = binding.etDescriptionAddProduct.text.toString()

                if (nama.isNotEmpty() && grade.isNotEmpty() && grade.isNotEmpty() && priceString.isNotEmpty() && weightString.isNotEmpty() && description.isNotEmpty()) {

                    val price = priceString.toInt()
                    val weight = weightString.toInt()

                    productViewModel.addProduct(file, nama, grade, price, weight, description).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoadingAddProduct(addProduct, true)
                                }

                                is ResultState.Success -> {
                                    showLoadingAddProduct(addProduct, false)
                                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                                    val shopIntent = Intent(this, ShopActivity::class.java)
                                    shopIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(shopIntent)
                                }

                                is ResultState.Error -> {
                                    showLoadingAddProduct(addProduct, false)
                                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
                }
            } ?: Toast.makeText(this, getString(R.string.invalid_image_empty), Toast.LENGTH_SHORT).show()
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToShopActivity()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val grade = resources.getStringArray(R.array.value_grade)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_list_grade, grade)

        binding.etGradeAddProduct.setAdapter(arrayAdapter)
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

    private fun showImagePickerDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.image_picker_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imgCamera: ImageView = dialog.findViewById(R.id.img_camera_image_picker_dialog)
        val imgGallery: ImageView = dialog.findViewById(R.id.img_gallery_image_picker_dialog)
        val btnCancel: Button = dialog.findViewById(R.id.btn_cancel_image_picker_dialog)

        imgCamera.setOnClickListener {
            imageUri = getImageUri(this)
            launchCamera.launch(imageUri)
            dialog.dismiss()
        }

        imgGallery.setOnClickListener {
            launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private val launchCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            showImage()
        }
    }

    private val launchGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        imageUri?.let {
            binding.imgAddProduct.setImageURI(it)
            binding.imgAddProduct.visibility = View.VISIBLE
            binding.imgChooseImage.visibility = View.GONE
            binding.tvChooseImage.visibility = View.GONE
        }
    }

    private fun navigateToShopActivity() {
        val intent = Intent(this, ShopActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showLoadingAddProduct(addProduct: MaterialButton, isLoading: Boolean) {
        if (!isLoading) {
            addProduct.text = getString(R.string.btn_add_product)
            addProduct.icon = AppCompatResources.getDrawable(this, R.drawable.icon_add_product)
        } else {
            addProduct.text = getString(R.string.btn_loading)
            addProduct.icon = null
        }
    }
}