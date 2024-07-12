package com.capstone.bloomy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.response.ProductData
import com.capstone.bloomy.databinding.ItemRowFreshCatchMarketBinding
import com.capstone.bloomy.ui.activity.MarketProductDetailActivity

class FreshCatchMarketAdapter : ListAdapter<ProductData, FreshCatchMarketAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowFreshCatchMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)

        holder.bind(product)
        holder.itemView.setOnClickListener {
            val detailProduct = Intent(holder.itemView.context, MarketProductDetailActivity::class.java)

            MarketProductDetailActivity.PRODUCT_ID = product.idProduct
            MarketProductDetailActivity.SELLER_USERNAME = product.usernameSeller

            holder.itemView.context.startActivity(detailProduct)
        }
    }

    class MyViewHolder(private val binding: ItemRowFreshCatchMarketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productData: ProductData) {
            Glide.with(binding.imgFreshCatchMarket)
                .load(productData.picture)
                .into(binding.imgFreshCatchMarket)

            binding.tvTitleFreshCatchMarket.text = productData.nama
            binding.tvGradeFreshCatchMarket.text = productData.grade
            binding.tvPriceFreshCatchMarket.text = formatCurrency(productData.price) + "/kg"
            binding.tvQuantityFreshCatchMarket.text = formatWeight(productData.weight)
            binding.tvLocationFreshCatchMarket.text = productData.kota
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductData>() {
            override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
                return oldItem == newItem
            }
        }

        fun formatCurrency(amount: Int): String {
            val formattedAmount = String.format("Rp%,d", amount)
            return formattedAmount.replace(',', '.')
        }

        fun formatWeight(weight: Int): String {
            return "$weight kg left"
        }
    }
}