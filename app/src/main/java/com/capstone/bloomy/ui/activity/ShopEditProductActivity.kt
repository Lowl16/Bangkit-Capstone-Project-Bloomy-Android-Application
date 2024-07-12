package com.capstone.bloomy.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProductByIdData
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.ActivityShopEditProductBinding
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory
import com.capstone.bloomy.utils.getImageUri
import com.capstone.bloomy.utils.reduceFileImage
import com.capstone.bloomy.utils.uriToFile
import com.google.android.material.button.MaterialButton

class ShopEditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopEditProductBinding

    private val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@ShopEditProductActivity)
    private val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

    private var imageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarShopEditProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extraProductId = intent.getStringExtra("product_id")

        productViewModel.getProductById(extraProductId.toString())
        productViewModel.detailProduct.observe(this) { detailProduct ->
            setDetailProduct(detailProduct)
            val grade = resources.getStringArray(R.array.value_grade)
            val arrayAdapter = ArrayAdapter(this, R.layout.item_list_grade, grade)

            binding.etGradeEditProduct.setAdapter(arrayAdapter)
        }

        binding.cardViewImageEditProduct.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnEdit.setOnClickListener {
            val editProduct = binding.btnEdit
            val title = binding.etTitleEditProduct.text.toString()
            val grade = binding.etGradeEditProduct.text.toString()
            val priceString = binding.etPriceKgEditProduct.text.toString()
            val weightString = binding.etQuantityKgEditProduct.text.toString()
            val description = binding.etDescriptionEditProduct.text.toString()

            if (title.isNotEmpty() && grade.isNotEmpty() && priceString.isNotEmpty() && weightString.isNotEmpty() && description.isNotEmpty()) {
                showLoadingEditProduct(editProduct, true)

                val price = priceString.toInt()
                val weight = weightString.toInt()

                productViewModel.editProduct(extraProductId.toString(), title, grade, price, weight, description)
                productViewModel.editProductResponse.observe(this@ShopEditProductActivity) { response ->
                    val error = response?.error
                    val message = response?.message.toString()

                    if (error != null) {
                        showLoadingEditProduct(editProduct, false)
                        if (error == true) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            productViewModel.defaultEditProduct()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            productViewModel.defaultEditProduct()

                            val shopIntent = Intent(this, ShopActivity::class.java)
                            shopIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(shopIntent)
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            }

            imageUri?.let { uri ->
                val file = uriToFile(uri, this).reduceFileImage()

                productViewModel.editPhotoProduct(extraProductId.toString(), file).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                showLoadingEditProduct(editProduct, true)
                            }

                            is ResultState.Success -> {
                                showLoadingEditProduct(editProduct, false)
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            }

                            is ResultState.Error -> {
                                showLoadingEditProduct(editProduct, false)
                                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val grade = resources.getStringArray(R.array.value_grade)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_list_grade, grade)

        binding.etGradeEditProduct.setAdapter(arrayAdapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
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
            binding.imgEditProduct.setImageURI(it)
        }
    }

    private fun setDetailProduct(detailProduct: ProductByIdData) {
        with(binding) {
            Glide.with(this@ShopEditProductActivity)
                .load(detailProduct.picture)
                .into(imgEditProduct)

            etTitleEditProduct.text = Editable.Factory.getInstance().newEditable(detailProduct.nama)
            etGradeEditProduct.text = Editable.Factory.getInstance().newEditable(detailProduct.grade)
            etPriceKgEditProduct.text = Editable.Factory.getInstance().newEditable(detailProduct.price.toString())
            etQuantityKgEditProduct.text = Editable.Factory.getInstance().newEditable(detailProduct.weight.toString())
            etDescriptionEditProduct.text = Editable.Factory.getInstance().newEditable(detailProduct.description)
        }
    }

    private fun showLoadingEditProduct(editProduct: MaterialButton, isLoading: Boolean) {
        if (!isLoading) {
            editProduct.text = getString(R.string.btn_edit)
            editProduct.icon = AppCompatResources.getDrawable(this, R.drawable.icon_edit_white)
        } else {
            editProduct.text = getString(R.string.btn_loading)
            editProduct.icon = null
        }
    }
}