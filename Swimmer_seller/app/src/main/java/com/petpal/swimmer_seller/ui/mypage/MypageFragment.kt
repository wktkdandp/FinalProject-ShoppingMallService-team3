package com.petpal.swimmer_seller.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.petpal.swimmer_seller.MainActivity
import com.petpal.swimmer_seller.R
import com.petpal.swimmer_seller.databinding.FragmentMypageBinding
import com.petpal.swimmer_seller.ui.user.UserViewModel
import com.petpal.swimmer_seller.ui.user.UserViewModelFactory

class MypageFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private var _fragmentMypageBinding: FragmentMypageBinding? = null
    private lateinit var mainActivity: MainActivity

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val fragmentMypageBinding get() = _fragmentMypageBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentMypageBinding = FragmentMypageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        return fragmentMypageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory())[UserViewModel::class.java]

        val textViewContactName = fragmentMypageBinding.textViewContactName
        val textViewBrandName = fragmentMypageBinding.textViewBrandName
        val buttonLogOut = fragmentMypageBinding.buttonLogOut

        userViewModel.run {
            //현재 사용자 정보 가져오기
            getCurrentSellerInfo()

            currentUser.observe(viewLifecycleOwner) { seller ->
                textViewBrandName.text = seller.brandName
                textViewContactName.text = seller.contactName+"님 안녕하세요!"
            }
        }

        buttonLogOut.setOnClickListener {
            userViewModel.logOut()
//            //메인 프래그먼트는 제거하고 로그인 프래그먼트로 이동
            findNavController().popBackStack(R.id.mainFragment, true)
//            findNavController().navigate(R.id.loginFragment)
            mainActivity.navigate(R.id.loginFragment)
        }
    }

}