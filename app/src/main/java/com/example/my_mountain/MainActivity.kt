package com.example.my_mountain

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.my_mountain.databinding.ActivityMainBinding
import com.example.my_mountain.ui.home.HomeFragment
import com.example.my_mountain.ui.location.LocationFragment
import com.example.my_mountain.ui.myPage.MyPageFragment
import com.example.my_mountain.ui.weather.WeatherFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 현재 선택된 Fragment ID 저장
    private var currentSelectedItemId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        bottomNaviClick()
    }

    private fun initView(){
        replaceFragment(MainFragmentName.HOME_FRAGMENT, false, null)
        binding.bottomNavi.selectedItemId = R.id.home_menu
    }
    //bottomNavi 클릭 이벤트
    private fun bottomNaviClick(){
        binding.bottomNavi.setOnItemSelectedListener {

            // 현재 선택된 아이템과 동일한 경우 아무 작업도 하지 않음
            if (currentSelectedItemId == it.itemId) {
                return@setOnItemSelectedListener true
            }

            when(it.itemId){
                R.id.home_menu -> {
                    replaceFragment(MainFragmentName.HOME_FRAGMENT, false, null)
                    true
                }
                R.id.compass_menu -> {
                    replaceFragment(MainFragmentName.LOCATION_FRAGMENT, false, null)
                    true
                }
                R.id.weather_menu -> {
                    replaceFragment(MainFragmentName.WEATHER_FRAGMENT, false, null)
                    true
                }
                R.id.my_page_menu -> {
                    replaceFragment(MainFragmentName.MY_PAGE_FRAGMENT, false, null)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    //Fragment 교체
    fun replaceFragment(name: MainFragmentName, addToBackStack: Boolean, data: Bundle?) {
        // 현재 표시된 프래그먼트와 같은 경우 아무 작업도 하지 않음
        if (currentSelectedItemId == name.id) {
            return
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 전환 애니메이션 추가
        fragmentTransaction.setCustomAnimations(
            android.R.anim.fade_in,  // 들어올 때
            android.R.anim.fade_out, // 나갈 때
            android.R.anim.fade_in,  // 다시 들어올 때 (뒤로가기)
            android.R.anim.fade_out  // 다시 나갈 때 (뒤로가기)
        )

        val fragment = when (name) {
            MainFragmentName.HOME_FRAGMENT -> HomeFragment()
            MainFragmentName.LOCATION_FRAGMENT -> LocationFragment()
            MainFragmentName.WEATHER_FRAGMENT -> WeatherFragment()
            MainFragmentName.MY_PAGE_FRAGMENT -> MyPageFragment()

        }

        if (data != null) {
            fragment.arguments = data
        }

        fragmentTransaction.replace(R.id.main_container, fragment)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(name.str)
        }
        fragmentTransaction.commit()

        // 현재 표시된 프래그먼트 이름 업데이트
        currentSelectedItemId = name.id
        binding.bottomNavi.selectedItemId = name.id
    }

    fun removeFragment() {
        SystemClock.sleep(200)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}