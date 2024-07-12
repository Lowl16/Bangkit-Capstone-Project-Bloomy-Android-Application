package com.capstone.bloomy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.model.TopNewsModel
import com.capstone.bloomy.databinding.ItemRowTopNewsBinding

class TopNewsAdapter(private val topNews: ArrayList<TopNewsModel>) : RecyclerView.Adapter<TopNewsAdapter.MyViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(var binding: ItemRowTopNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowTopNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (title, imageUrl, _) = topNews[position]

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.imgTopNews)

        holder.binding.tvTopNews.text = title

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(topNews[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = topNews.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(topNews: TopNewsModel)
    }
}