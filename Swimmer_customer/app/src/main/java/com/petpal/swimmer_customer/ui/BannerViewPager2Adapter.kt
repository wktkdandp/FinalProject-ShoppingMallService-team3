package com.petpal.swimmer_customer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.ProductDetailModel


class BannerViewPager2Adapter : ListAdapter<ProductDetailModel, BannerViewPager2Adapter.ItemViewHolder>(
    differ
) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(ranking: ProductDetailModel) {
            val ImageView = view.findViewById<ImageView>(R.id.productDetailImage)

            Glide
                .with(ImageView.context)
                .load(ranking.image)
                .into(ImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.activity_product_detail_viewpage2, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        val differ = object : DiffUtil.ItemCallback<ProductDetailModel>() {
            override fun areItemsTheSame(oldItem: ProductDetailModel, newItem: ProductDetailModel): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: ProductDetailModel, newItem: ProductDetailModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}