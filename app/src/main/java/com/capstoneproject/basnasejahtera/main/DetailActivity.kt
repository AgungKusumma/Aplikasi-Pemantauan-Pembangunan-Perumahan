package com.capstoneproject.basnasejahtera.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.databinding.ActivityDetailBinding
import com.capstoneproject.basnasejahtera.model.ItemData

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupData()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupData() {
        val rumah = intent.getParcelableExtra<ItemData>("Rumah") as ItemData
        binding.apply {
            Glide.with(applicationContext)
                .load(rumah.photo)
                .circleCrop()
                .into(ivHouse)
            tvBlok.text = rumah.id
            tvName.text = rumah.nama
            tvStatus.text = "${rumah.status}%"
            progressBar.progress = rumah.status
            textViewProgress.text = "${rumah.status}%"

            when (rumah.info) {
                "SOLD" -> {
                    binding.ivHouse.setBackgroundColor(Color.GREEN)
                }
                "BOOKED" -> {
                    binding.ivHouse.setBackgroundColor(Color.YELLOW)
                }
                "SELL" -> {
                    binding.ivHouse.setBackgroundColor(Color.WHITE)
                }
            }
        }
    }
}