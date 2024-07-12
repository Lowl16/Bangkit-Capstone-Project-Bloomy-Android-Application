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
import com.capstone.bloomy.data.response.PurchasesTransactionData
import com.capstone.bloomy.databinding.ItemRowTransactionBinding
import com.capstone.bloomy.ui.activity.PurchasesTransactionDetailActivity

class TransactionPurchasesAdapter : ListAdapter<PurchasesTransactionData, TransactionPurchasesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = getItem(position)

        holder.bind(transaction)
        holder.itemView.setOnClickListener {
            val detailTransaction = Intent(holder.itemView.context, PurchasesTransactionDetailActivity::class.java)

            PurchasesTransactionDetailActivity.TRANSACTION_ID = transaction.idTransaction
            PurchasesTransactionDetailActivity.SELLER_USERNAME = transaction.sellerData.usernameSeller

            holder.itemView.context.startActivity(detailTransaction)
        }
    }

    class MyViewHolder(private val binding: ItemRowTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(purchasesTransactionData: PurchasesTransactionData) {
            val context = binding.root.context

            Glide.with(binding.imgUserTransaction)
                .load(purchasesTransactionData.sellerData.picture)
                .into(binding.imgUserTransaction)

            Glide.with(binding.imgProductTransaction)
                .load(purchasesTransactionData.productPurchaseData.picture)
                .into(binding.imgProductTransaction)

            binding.tvUserTransaction.text = purchasesTransactionData.sellerData.nameSeller
            binding.tvStatusTransaction.text = when (purchasesTransactionData.status) {
                "0" -> context.getString(R.string.not_confirmed)
                "1" -> context.getString(R.string.in_process)
                "2" -> context.getString(R.string.shipped)
                "3" -> context.getString(R.string.finished)
                "4" -> context.getString(R.string.canceled)
                else -> "Unknown"
            }
            binding.tvGradeTransaction.text = purchasesTransactionData.productPurchaseData.grade
            binding.tvTitleTransaction.text = purchasesTransactionData.productPurchaseData.nama
            binding.tvPriceTransaction.text = formatCurrency(purchasesTransactionData.productPurchaseData.pricePerKg.toInt()) + "/kg"
            binding.tvDeliveryMethodTransaction.text = when (purchasesTransactionData.type) {
                "0" -> context.getString(R.string.delivery)
                "1" -> context.getString(R.string.self_pickup)
                else -> "Unknown"
            }
            binding.tvQuantityTransaction.text = purchasesTransactionData.weight.toString()
            binding.tvTotalTransaction.text = "Total: " + formatCurrency(purchasesTransactionData.price)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PurchasesTransactionData>() {
            override fun areItemsTheSame(oldItem: PurchasesTransactionData, newItem: PurchasesTransactionData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PurchasesTransactionData, newItem: PurchasesTransactionData): Boolean {
                return oldItem == newItem
            }
        }

        fun formatCurrency(amount: Int): String {
            val formattedAmount = String.format("Rp%,d", amount)
            return formattedAmount.replace(',', '.')
        }
    }
}