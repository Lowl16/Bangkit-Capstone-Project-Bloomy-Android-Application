package com.capstone.bloomy.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.response.ProductByUsernameData
import com.capstone.bloomy.data.response.ProfileByUsernameData
import com.capstone.bloomy.databinding.ActivityMarketSellerDetailBinding
import com.capstone.bloomy.ui.adapter.ProductSellerDetailAdapter
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory

class MarketSellerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketSellerDetailBinding

    private val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@MarketSellerDetailActivity)
    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketSellerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarSellerDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extraSellerUsername = intent.getStringExtra("seller_username")

        profileViewModel.getProfileByUsername(extraSellerUsername.toString())
        profileViewModel.profileByUsername.observe(this) { profile ->
            setProfileByUsername(profile)

            val profileByUsernameData: ProfileByUsernameData = profile ?: return@observe

            val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@MarketSellerDetailActivity)
            val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

            productViewModel.getProductByUsername(profileByUsernameData.username)
            productViewModel.product.observe(this) { product ->
                setProductByUsernameData(product)
            }
        }
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

    private fun setProfileByUsername(profileByUsername: ProfileByUsernameData) {
        with(binding) {
            Glide.with(this@MarketSellerDetailActivity)
                .load(profileByUsername.photo)
                .into(imgSellerDetail)

            tvNameSellerDetail.text = profileByUsername.nama
            tvPhoneSellerDetail.text = profileByUsername.nohp
            tvLocationSellerDetail.text = profileByUsername.kota
        }
    }

    private fun setProductByUsernameData(product: List<ProductByUsernameData>) {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProductSellerDetail.layoutManager = layoutManager

        val adapter = ProductSellerDetailAdapter()
        adapter.submitList(product)
        binding.recyclerViewProductSellerDetail.adapter = adapter

        val itemCount = adapter.itemCount
        if (itemCount != 0) {
            binding.tvProductSellerDetail.text = "$itemCount Product"
        }
    }
}