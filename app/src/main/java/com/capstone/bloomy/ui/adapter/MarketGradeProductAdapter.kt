package com.capstone.bloomy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.response.ProductByGradeData
import com.capstone.bloomy.databinding.ItemRowMarketGradeProductBinding
import com.capstone.bloomy.ui.activity.MarketProductDetailActivity

class MarketGradeProductAdapter : ListAdapter<ProductByGradeData, MarketGradeProductAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowMarketGradeProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    class MyViewHolder(private val binding: ItemRowMarketGradeProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productByGradeData: ProductByGradeData) {
            Glide.with(binding.imgMarketGradeProduct)
                .load(productByGradeData.picture)
                .into(binding.imgMarketGradeProduct)

            binding.tvTitleMarketGradeProduct.text = productByGradeData.nama
            binding.tvPriceMarketGradeProduct.text = formatCurrency(productByGradeData.price) + "/kg"
            binding.tvQuantityMarketGradeProduct.text = formatWeight(productByGradeData.weight)
            binding.tvLocationMarketGradeProduct.text = productByGradeData.kota
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductByGradeData>() {
            override fun areItemsTheSame(oldItem: ProductByGradeData, newItem: ProductByGradeData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductByGradeData, newItem: ProductByGradeData): Boolean {
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