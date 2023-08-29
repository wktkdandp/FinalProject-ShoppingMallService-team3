package com.petpal.swimmer_customer.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.petpal.swimmer_customer.data.model.Product
import com.petpal.swimmer_customer.databinding.RankingItemBinding
import com.petpal.swimmer_customer.ui.home.HomeFragmentDirections
import java.text.NumberFormat
import java.util.Locale

class HomeFragmentAdapter(val context: Context, private val dataSet: List<Product>) :
    RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(item: RankingItemBinding) : RecyclerView.ViewHolder(item.root) {
        var imageView: ImageView = item.rankingImageView
        var brand: TextView = item.rankingBrand
        var title: TextView = item.rankingTitle
        var description: TextView = item.rankingDescription

        init {
            item.root.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionItemHomeToItemDetailFragment(adapterPosition)
                item.root.findNavController().navigate(action)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rankingItemBinding =
            RankingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolderClass = ViewHolder(rankingItemBinding)
        return viewHolderClass
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imagePathList = mutableListOf<String>()

        // 이미지 경로들 추출 및 공백과 , 제거
        val paths = dataSet[position].mainImage[position].toString()
            .substring(1, dataSet[position].mainImage[position].toString().length - 1).split(",")

        for (path in paths) {
            val cleanedPath = path.trim() // 좌우 공백 제거
            imagePathList.add(cleanedPath)
        }
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pathRef = storageRef.child(imagePathList[0])

        pathRef.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(holder.imageView)
        }


        val originalText = dataSet[position].name
        val formattedText = formatText(originalText, 6)
        holder.brand.text = dataSet[position].brandName
        holder.title.text = formattedText
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val formattedValues = numberFormat.format(dataSet[position].price)
        holder.description.text = "$formattedValues 원"
    }

    // 글자 자르는 함수 (ex: 6글자 자르고 다음 칸으로..)
    fun formatText(text: String, maxLength: Int): String {
        val regex = ".{1,$maxLength}".toRegex()
        return regex.replace(text, "\$0\n")
    }
}