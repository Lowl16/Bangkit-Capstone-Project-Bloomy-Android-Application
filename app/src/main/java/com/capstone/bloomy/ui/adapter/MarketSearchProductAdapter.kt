package com.capstone.bloomy.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.response.ProductByNameData
import com.capstone.bloomy.databinding.ItemRowMarketSearchProductBinding
import com.capstone.bloomy.ui.activity.MarketProductDetailActivity

class MarketSearchProductAdapter : ListAdapter<ProductByNameData, MarketSearchProductAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowMarketSearchProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyViewHolder(private val binding: ItemRowMarketSearchProductBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(productByNameData: ProductByNameData) {
            Glide.with(binding.imgMarketSearchProduct)
                .load(productByNameData.picture)
                .into(binding.imgMarketSearchProduct)

            binding.tvTitleMarketSearchProduct.text = productByNameData.nama
            binding.tvGradeMarketSearchProduct.text = productByNameData.grade
            binding.tvPriceMarketSearchProduct.text = formatCurrency(productByNameData.price) + "/kg"
            binding.tvQuantityMarketSearchProduct.text = formatWeight(productByNameData.weight)
            binding.tvLocationMarketSearchProduct.text = productByNameData.kota
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductByNameData>() {
            override fun areItemsTheSame(oldItem: ProductByNameData, newItem: ProductByNameData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductByNameData, newItem: ProductByNameData): Boolean {
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