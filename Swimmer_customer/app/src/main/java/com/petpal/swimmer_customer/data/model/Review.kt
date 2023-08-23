package com.petpal.swimmer_customer.data.model

var exReviewList = mutableListOf<Review>()

data class Review(
    val reviewId: Long,
    val userId: Long,
    val height: Long,
    val weight: Long,
    val rating: Double,
    val date: String,
    val content: String,
    val image: String,
    val size: Long,
    val color: String
)