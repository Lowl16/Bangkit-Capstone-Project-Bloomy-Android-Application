package com.capstone.bloomy.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.SalesTransactionData
import com.capstone.bloomy.databinding.ItemRowTransactionBinding
import com.capstone.bloomy.ui.activity.SalesTransactionDetailActivity

class TransactionSalesAdapter : ListAdapter<SalesTransactionData, TransactionSalesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = getItem(position)

        holder.bind(transaction)
        holder.itemView.setOnClickListener {
            val detailTransaction = Intent(holder.itemView.context, SalesTransactionDetailActivity::class.java)

            SalesTransactionDetailActivity.TRANSACTION_ID = transaction.idTransaction
            SalesTransactionDetailActivity.BUYER_USERNAME = transaction.buyerData.usernameBuyer

            holder.itemView.context.startActivity(detailTransaction)
        }
    }

    class MyViewHolder(private val binding: ItemRowTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(salesTransactionData: SalesTransactionData) {
            val context = binding.root.context

            Glide.with(binding.imgUserTransaction)
                .load(salesTransactionData.buyerData.pictureBuyer)
                .into(binding.imgUserTransaction)

            Glide.with(binding.imgProductTransaction)
                .load(salesTransactionData.productSaleData.picture)
                .into(binding.imgProductTransaction)

            binding.tvUserTransaction.text = salesTransactionData.buyerData.namaBuyer
            binding.tvStatusTransaction.text = when (salesTransactionData.status) {
                "0" -> context.getString(R.string.not_confirmed)
                "1" -> context.getString(R.string.in_process)
                "2" -> context.getString(R.string.shipped)
                "3" -> context.getString(R.string.finished)
                "4" -> context.getString(R.string.canceled)
                else -> "Unknown"
            }
            binding.tvGradeTransaction.text = salesTransactionData.productSaleData.grade
            binding.tvTitleTransaction.text = salesTransactionData.productSaleData.nama
            binding.tvPriceTransaction.text = formatCurrency(salesTransactionData.productSaleData.pricePerKg.toInt()) + "/kg"
            binding.tvDeliveryMethodTransaction.text = when (salesTransactionData.type) {
                "0" -> context.getString(R.string.delivery)
                "1" -> context.getString(R.string.self_pickup)
                else -> "Unknown"
            }
            binding.tvQuantityTransaction.text = salesTransactionData.weight.toString()
            binding.tvTotalTransaction.text = "Total: " + formatCurrency(salesTransactionData.price)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SalesTransactionData>() {
            override fun areItemsTheSame(oldItem: SalesTransactionData, newItem: SalesTransactionData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SalesTransactionData, newItem: SalesTransactionData): Boolean {
                return oldItem == newItem
            }
        }

        fun formatCurrency(amount: Int): String {
            val formattedAmount = String.format("Rp%,d", amount)
            return formattedAmount.replace(',', '.')
        }
    }
}