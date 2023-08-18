package com.petpal.swimmer_seller.data.repository

import com.petpal.swimmer_seller.data.model.Seller


class SellerRepository {
    companion object {
        fun getSellerByIdx(sellerIdx: Long) {

        }

//        fun getAllSeller(): Seller {
//
//        }

        fun addSeller(seller: Seller): Boolean {

            return true
        }

        fun modifySeller(sellerIdx: Long, seller: Seller): Boolean {

            return true
        }

    }
}
