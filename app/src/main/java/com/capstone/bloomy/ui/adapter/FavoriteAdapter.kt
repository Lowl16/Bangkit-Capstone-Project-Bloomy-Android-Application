package com.capstone.bloomy.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.response.FavoriteData
import com.capstone.bloomy.databinding.ItemRowFavoriteBinding
import com.capstone.bloomy.ui.activity.MarketProductDetailActivity

class FavoriteAdapter : ListAdapter<FavoriteData, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)

        holder.bind(product)
        holder.itemView.setOnClickListener {
            val detailProduct = Intent(holder.itemView.context, MarketProductDetailActivity::class.java)

            MarketProductDetailActivity.PRODUCT_ID = product.idProduct

            holder.itemView.context.startActivity(detailProduct)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyViewHolder(private val binding: ItemRowFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(favoriteData: FavoriteData) {
            Glide.with(binding.imgFavorite)
                .load(favoriteData.picture)
                .into(binding.imgFavorite)

            binding.tvTitleFavorite.text = favoriteData.nama
            binding.tvGradeFavorite.text = favoriteData.grade
            binding.tvPriceFavorite.text = formatCurrency(favoriteData.price) + "/kg"
            binding.tvQuantityFavorite.text = formatWeight(favoriteData.weight)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteData>() {
            override fun areItemsTheSame(oldItem: FavoriteData, newItem: FavoriteData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteData, newItem: FavoriteData): Boolean {
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