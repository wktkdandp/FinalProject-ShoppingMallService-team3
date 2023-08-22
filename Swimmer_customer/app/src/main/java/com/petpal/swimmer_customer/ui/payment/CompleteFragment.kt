package com.petpal.swimmer_customer.ui.payment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.petpal.swimmer_customer.R
import com.petpal.swimmer_customer.databinding.FragmentCompleteBinding
import com.petpal.swimmer_customer.ui.main.MainActivity

class CompleteFragment : Fragment() {

    lateinit var fragmentCompleteBinding: FragmentCompleteBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCompleteBinding = FragmentCompleteBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentCompleteBinding.run {

            // countdownTimer 로 사용자가 클릭을 하지 않아도 자동 화면 전환
            val countDownTimer = object: CountDownTimer(4000, 1000) {

                override fun onTick(p0: Long) {
                    completeTextCountdown.text = "${(p0 / 1000)}초 뒤 홈 화면으로 이동합니다."
                }

                // timer countdown 종료 시 실행되는 메서드
                override fun onFinish() {

                    // homeFragment로 이동
                    Navigation.findNavController(fragmentCompleteBinding.root)
                        .navigate(R.id.item_home)
                }

            }.start()

            completeHome.setOnClickListener {
                countDownTimer.cancel()
                // homeFragment로 이동
                Navigation.findNavController(fragmentCompleteBinding.root)
                    .navigate(R.id.item_home)
            }
        }

        return fragmentCompleteBinding.root
    }
}