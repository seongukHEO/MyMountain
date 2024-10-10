package com.example.my_mountain.ui.addMountain

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.my_mountain.R
import com.example.my_mountain.databinding.ActivityAddMountainBinding
import com.google.android.gms.maps.MapsInitializer

class AddMountainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMountainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

        binding = ActivityAddMountainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}