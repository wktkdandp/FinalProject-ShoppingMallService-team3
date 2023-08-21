package com.petpal.swimmer_customer.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petpal.swimmer_customer.databinding.RankingItemBinding
import com.petpal.swimmer_customer.data.model.Ranking

class HomeFragmentAdapter(val context: Context, private val dataSet: List<Ranking>) :
    RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(item: RankingItemBinding) : RecyclerView.ViewHolder(item.root) {
        var imageView: ImageView = item.rankingImageView
        var title: TextView = item.rankingTitle
        var description: TextView = item.rankingDescription

        init {
            item.root.setOnClickListener {
                val selectedIdx = dataSet[adapterPosition].rankingIdx
                val action = HomeFragmentDirections.actionItemHomeToItemDetailFragment(selectedIdx)
                item.root.findNavController().navigate(action)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rankingItemBinding = RankingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolderClass = ViewHolder(rankingItemBinding)
        return viewHolderClass
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide
            .with(context)
            .load(dataSet[position].rankingImage)
            .into(holder.imageView)

        val originalText = dataSet[position].title
        val formattedText = formatText(originalText, 6)

        holder.title.text = formattedText
        holder.description.text = dataSet[position].price
    }

    // 글자 자르는 함수 (ex: 6글자 자르고 다음 칸으로..)
    fun formatText(text: String, maxLength: Int): String {
        val regex = ".{1,$maxLength}".toRegex()
        return regex.replace(text, "\$0\n")
    }
}