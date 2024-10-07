package com.example.my_mountain.ui.home.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_mountain.databinding.ItemHomeReBinding

class HomeAdapter(): RecyclerView.Adapter<HomeViewHolder>() {

    private lateinit var recyclerviewClick: ItemOnClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val itemHomeReBinding = ItemHomeReBinding.inflate(layoutInflater)
        val viewHolder = HomeViewHolder(itemHomeReBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.itemHomeReBinding.root.setOnClickListener {
            recyclerviewClick.recyclerviewClickListener()
        }
    }

    fun setRecyclerviewClick(recyclerviewClick: ItemOnClickListener){
        this.recyclerviewClick = recyclerviewClick
    }

    interface ItemOnClickListener{
        fun recyclerviewClickListener()
    }
}

class HomeViewHolder(itemHomeReBinding: ItemHomeReBinding): RecyclerView.ViewHolder(itemHomeReBinding.root){
    var itemHomeReBinding: ItemHomeReBinding

    init {
        this.itemHomeReBinding = itemHomeReBinding
        this.itemHomeReBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}