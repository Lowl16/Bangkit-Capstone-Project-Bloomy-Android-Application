package com.capstone.bloomy.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProductByNameData
import com.capstone.bloomy.databinding.ActivityMarketSearchProductBinding
import com.capstone.bloomy.ui.adapter.MarketSearchProductAdapter
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory

class MarketSearchProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketSearchProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketSearchProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarMarketSearchProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extraSearchQuery = intent.getStringExtra("search_query")

        with(binding) {
            searchBarMarketSearchProduct.setText(extraSearchQuery)
            searchViewMarketSearchProduct.setupWithSearchBar(searchBarMarketSearchProduct)
            searchViewMarketSearchProduct
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchViewMarketSearchProduct.text.toString()
                        searchViewMarketSearchProduct.hide()

                        if (query.isNotEmpty()) { handleSearch(query) }

                        return@setOnEditorActionListener true
                    }
                    false
                }
        }

        val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(this@MarketSearchProductActivity)
        val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

        productViewModel.getProductByName(extraSearchQuery.toString())
        productViewModel.productByName.observe(this) { products ->
            setProduct(products)
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

    @SuppressLint("SetTextI18n")
    private fun setProduct(productByName: List<ProductByNameData>) {
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewMarketSearchProduct.layoutManager = layoutManager

        val adapter = MarketSearchProductAdapter()
        adapter.submitList(productByName)
        binding.recyclerViewMarketSearchProduct.adapter = adapter

        val itemCount = adapter.itemCount
        val extraSearchQuery = intent.getStringExtra("search_query")

        val resultCountText = getString(
            if (itemCount == 1) R.string.search_result_single else R.string.search_result_plural,
            itemCount
        )

        val description = getString(R.string.search_description, itemCount, resultCountText, extraSearchQuery ?: "No Search Query")
        binding.tvDescriptionMarketSearchProduct.text = description
    }

    private fun handleSearch(query: String) {
        val intent = Intent(this, MarketSearchProductActivity::class.java)
        intent.putExtra("search_query", query)
        startActivity(intent)
    }
}