package com.example.my_mountain.ui.myPage

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.my_mountain.MainActivity
import com.example.my_mountain.R
import com.example.my_mountain.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {

    lateinit var binding: FragmentMyPageBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        clickImage()
        return binding.root
    }

    private fun clickImage(){
        binding.apply {
            imagePepe.setOnClickListener {
                lottiePepe.visibility = LottieAnimationView.VISIBLE
                lottiePepe.playAnimation()

                Handler().postDelayed({
                    lottiePepe.cancelAnimation()
                    lottiePepe.visibility = LottieAnimationView.GONE
                }, 5000)
            }
        }
    }
}