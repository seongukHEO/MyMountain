package com.example.my_mountain.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_mountain.MainActivity
import com.example.my_mountain.R
import com.example.my_mountain.databinding.FragmentHomeBinding
import com.example.my_mountain.ui.addMountain.AddMountainActivity
import com.example.my_mountain.ui.home.adapter.HomeAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    lateinit var mainActivity: MainActivity

    val homeAdapter : HomeAdapter by lazy {
        val adapter = HomeAdapter()
        adapter.setRecyclerviewClick(object : HomeAdapter.ItemOnClickListener{
            override fun recyclerviewClickListener() {

            }

        })
        adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        homeAdapterAll()
        clickFloat()

        return binding.root
    }

    private fun homeAdapterAll(){
        binding.apply {
            recyclerviewHome.apply {
                adapter = homeAdapter
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    //플로팅 버튼
    private fun clickFloat(){
        binding.apply {
            floatingActionButton.setOnClickListener {
                startActivity(Intent(mainActivity, AddMountainActivity::class.java))
            }
        }
    }
}