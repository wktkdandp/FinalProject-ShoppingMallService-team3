package com.petpal.swimmer_customer.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.petpal.swimmer_customer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    // 사용자가 누른 항목 번호
    var rowPosition = 0

    companion object {
        // Activity가 관리할 프래그먼트들의 이름
        val Login_Fragment = "LoginFragment"
        val Main_Fragment = "MainFragment"
        val Register_Fragment="RegisterFragment"
        val Find_Info_Fragment="FindInfoFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


       // replaceFragment(Login_Fragment, false, false)
    }

    // 지정한 Fragment를 보여주는 메서드
//    fun replaceFragment(name:String, addToBackStack:Boolean, animate:Boolean){
//        // Fragment 교체 상태로 설정한다.
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        // 새로운 Fragment를 담을 변수
//        var newFragment = when(name){
//            Main_Fragment -> {
//                MainFragment()
//            }
//            Login_Fragment->{
//                LoginFragment()
//            }
//            Register_Fragment->{
//                RegisterFragment()
//            }
//            Find_Info_Fragment->{
//                FindInfoFragment()
//            }
//
//            else -> {
//                Fragment()
//            }
//        }
//
//        if(newFragment != null) {
//            // Fragment를 교채한다.
//            fragmentTransaction.replace(R.id.mainContainer, newFragment)
//
//            if (animate == true) {
//                // 애니메이션을 설정한다.
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            }
//
//            if (addToBackStack == true) {
//                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
//                fragmentTransaction.addToBackStack(name)
//            }
//
//            // 교체 명령이 동작하도록 한다.
//            fragmentTransaction.commit()
//        }
//    }
//    // Fragment를 BackStack에서 제거한다.
//    fun removeFragment(name:String){
//        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//    }


}