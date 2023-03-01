package com.capstoneproject.basnasejahtera.konsumen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.basnasejahtera.databinding.ActivityPanduanBinding

class PanduanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPanduanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanduanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}