package com.petpal.swimmer_customer.ui.product.tablayout

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.data.model.Review
import com.petpal.swimmer_customer.databinding.FragmentProductReviewTabReviewItemBinding

class ReviewAdapter(val context: Context, private var dataSet: MutableList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(item: FragmentProductReviewTabReviewItemBinding) :
        RecyclerView.ViewHolder(item.root) {
        val height: TextView = item.reviewHeightTextView
        val weight: TextView = item.reviewWeightTextView
        val rating: RatingBar = item.reviewCustomRatingBar
        val date: TextView = item.reviewCreationDateTextView
        val content: TextView = item.reviewItemEvaluationDetailText
        val image: ImageView = item.reviewImageView
        val size: TextView = item.reviewSizeTextView
        val color: TextView = item.reviewColorTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val reviewItemBinding = FragmentProductReviewTabReviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolderClass = ViewHolder(reviewItemBinding)
        return viewHolderClass
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide
            .with(context)
            .load(dataSet[position].image)
            .error(R.drawable.noimg)
            .into(holder.image)

        holder.height.text = "${dataSet[position].height.toString()}cm , "
        holder.weight.text = "${dataSet[position].weight.toString()}kg"
        holder.rating.rating = dataSet[position].rating.toFloat()
        holder.date.text = dataSet[position].date.toString()
        holder.content.text = dataSet[position].content.toString()
        holder.size.text = "사이즈 : ${dataSet[position].size.toString()} , "
        holder.color.text = "색상 : ${dataSet[position].color.toString()}"
    }
}