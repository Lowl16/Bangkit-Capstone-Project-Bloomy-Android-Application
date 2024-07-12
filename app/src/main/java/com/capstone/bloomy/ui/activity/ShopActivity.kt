package com.capstone.bloomy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProductByUsernameData
import com.capstone.bloomy.data.response.ProfileData
import com.capstone.bloomy.databinding.ActivityShopBinding
import com.capstone.bloomy.ui.adapter.ProductShopAdapter
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding

    private val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@ShopActivity)
    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarShop)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profileViewModel.getProfile()

        profileViewModel.profile.observe(this) { profile ->
            setProfile(profile)

            val profileData: ProfileData = profile ?: return@observe

            val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@ShopActivity)
            val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

            productViewModel.getProductByUsername(profileData.username)
            productViewModel.product.observe(this) { product ->
                setProductByUsernameData(product)
            }
        }

        binding.btnAddProduct.setOnClickListener {
            profileViewModel.getProfile()

            profileViewModel.profile.observe(this) { profile ->
                if (profile.nama.isNullOrEmpty() && profile.nohp.isNullOrEmpty() && profile.provinsi.isNullOrEmpty() && profile.kota.isNullOrEmpty() && profile.alamat.isNullOrEmpty() && profile.description.isNullOrEmpty()) {
                    val editProfileIntent = Intent(this, EditProfileActivity::class.java)
                    startActivity(editProfileIntent)
                } else {
                    val shopAddProductIntent = Intent(this, ShopAddProductActivity::class.java)
                    startActivity(shopAddProductIntent)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToMainActivity()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        profileViewModel.getProfile()

        profileViewModel.profile.observe(this) { profile ->
            setProfile(profile)

            val profileData: ProfileData = profile ?: return@observe

            val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@ShopActivity)
            val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

            productViewModel.getProductByUsername(profileData.username)
            productViewModel.product.observe(this) { product ->
                setProductByUsernameData(product)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        profileViewModel.getProfile()

        profileViewModel.profile.observe(this) { profile ->
            setProfile(profile)

            val profileData: ProfileData = profile ?: return@observe

            val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@ShopActivity)
            val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

            productViewModel.getProductByUsername(profileData.username)
            productViewModel.product.observe(this) { product ->
                setProductByUsernameData(product)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToMainActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setProfile(profile: ProfileData) {
        with(binding) {
            Glide.with(this@ShopActivity)
                .load(profile.photo)
                .into(imgShop)

            tvNameShop.text = profile.nama.ifEmpty { getString(R.string.default_name) }
            tvEmailShop.text = profile.email.ifEmpty { getString(R.string.default_email) }
            tvLocationShop.text = profile.kota.ifEmpty { getString(R.string.default_location) }
        }
    }

    private fun setProductByUsernameData(product: List<ProductByUsernameData>) {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProductShop.layoutManager = layoutManager

        val adapter = ProductShopAdapter()
        adapter.submitList(product)
        binding.recyclerViewProductShop.adapter = adapter

        val itemCount = adapter.itemCount
        if (itemCount != 0) {
            binding.tvProductShop.text = "$itemCount Product"
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateToProfileFragment", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
