package com.petpal.swimmer_seller.ui.user

data class UserResult(
    //성공하면 보여줄 값
    val successInt: Int? = null,
    val successString: String? = null,
    val error: Int? = null
)