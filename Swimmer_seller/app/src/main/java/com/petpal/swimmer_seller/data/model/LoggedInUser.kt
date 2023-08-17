package com.petpal.swimmer_seller.data.model

/**
 * Data class that captures user information for logged in users retrieved from UserRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String
)