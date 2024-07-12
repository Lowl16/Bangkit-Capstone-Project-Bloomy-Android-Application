package com.capstone.bloomy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.response.ProductByUsernameData
import com.capstone.bloomy.databinding.ItemRowProductShopGridBinding
import com.capstone.bloomy.ui.activity.MarketProductDetailActivity

class ProductSellerDetailAdapter : ListAdapter<ProductByUsernameData, ProductSellerDetailAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowProductShopGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    class MyViewHolder(private val binding: ItemRowProductShopGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productByUsernameData: ProductByUsernameData) {
            Glide.with(binding.imgProductShopGrid)
                .load(productByUsernameData.picture)
                .into(binding.imgProductShopGrid)

            binding.tvTitleProductShopGrid.text = productByUsernameData.nama
            binding.tvGradeProductShopGrid.text = productByUsernameData.grade
            binding.tvPriceProductShopGrid.text = formatCurrency(productByUsernameData.price) + "/kg"
            binding.tvQuantityProductShopGrid.text = formatWeight(productByUsernameData.weight)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductByUsernameData>() {
            override fun areItemsTheSame(oldItem: ProductByUsernameData, newItem: ProductByUsernameData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductByUsernameData, newItem: ProductByUsernameData): Boolean {
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