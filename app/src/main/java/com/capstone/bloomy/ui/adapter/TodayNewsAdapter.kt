package com.capstone.bloomy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bloomy.data.model.TodayNewsModel
import com.capstone.bloomy.databinding.ItemRowTodayNewsBinding

class TodayNewsAdapter(private val todayNews: ArrayList<TodayNewsModel>) : RecyclerView.Adapter<TodayNewsAdapter.MyViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(var binding: ItemRowTodayNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowTodayNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (title, date, imageUrl, _) = todayNews[position]

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.imgTodayNews)

        holder.binding.tvTitleTodayNews.text = title

        holder.binding.tvDateTodayNews.text = date

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(todayNews[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = todayNews.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(todayNews: TodayNewsModel)
    }
}