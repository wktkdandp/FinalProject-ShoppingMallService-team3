package com.petpal.swimmer_customer.ui.product.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD:Swimmer_customer/app/src/main/java/com/petpal/swimmer_customer/HomeFragment.kt
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petpal.swimmer_customer.databinding.FragmentHomeBinding
import com.petpal.swimmer_customer.ui.main.MainFragmentDirections
=======
import com.petpal.swimmer_customer.R
>>>>>>> upstream/main:Swimmer_customer/app/src/main/java/com/petpal/swimmer_customer/ui/product/tablayout/ProductQnATabFragment.kt


<<<<<<< HEAD:Swimmer_customer/app/src/main/java/com/petpal/swimmer_customer/HomeFragment.kt
class HomeFragment : Fragment() {
    lateinit var fragmentHomeBinding: FragmentHomeBinding
=======
/**
 * A simple [Fragment] subclass.
 * Use the [ProductQnATabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductQnATabFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

>>>>>>> upstream/main:Swimmer_customer/app/src/main/java/com/petpal/swimmer_customer/ui/product/tablayout/ProductQnATabFragment.kt
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
<<<<<<< HEAD:Swimmer_customer/app/src/main/java/com/petpal/swimmer_customer/HomeFragment.kt
        fragmentHomeBinding= FragmentHomeBinding.inflate(layoutInflater)

        // 자동로그인 로그아웃 구현용
        fragmentHomeBinding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
            findNavController().navigate(action)
        }


        return fragmentHomeBinding.root
    }


=======
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_qn_a_tab, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductQnATabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductQnATabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
>>>>>>> upstream/main:Swimmer_customer/app/src/main/java/com/petpal/swimmer_customer/ui/product/tablayout/ProductQnATabFragment.kt
}