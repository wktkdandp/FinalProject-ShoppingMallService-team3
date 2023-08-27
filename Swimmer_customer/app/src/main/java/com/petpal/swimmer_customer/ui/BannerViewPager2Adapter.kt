package com.petpal.swimmer_customer.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.ProductDetailModel


class BannerViewPager2Adapter(
    var context: Context,
    var homeFragmentItemList: MutableList<ProductDetailModel>
) : ListAdapter<ProductDetailModel, BannerViewPager2Adapter.ItemViewHolder>(
    differ
) {

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            inflater.inflate(
                R.layout.activity_product_detail_viewpage2,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val imageView = holder.itemView.findViewById<ImageView>(R.id.productDetailImage)
        val imagePath = homeFragmentItemList[position]

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pathRef = storageRef.child("image/${imagePath.image}")
        Log.d("여름", "image/$imagePath")

        pathRef.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView)
        }
    }

    companion object {

        val differ = object : DiffUtil.ItemCallback<ProductDetailModel>() {
            override fun areItemsTheSame(
                oldItem: ProductDetailModel,
                newItem: ProductDetailModel
            ): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(
                oldItem: ProductDetailModel,
                newItem: ProductDetailModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}