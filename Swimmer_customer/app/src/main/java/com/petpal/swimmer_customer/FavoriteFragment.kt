package com.petpal.swimmer_customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petpal.swimmer_customer.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    lateinit var favoriteBinding: FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteBinding= FragmentFavoriteBinding.inflate(inflater)
        return favoriteBinding.root
    }


}