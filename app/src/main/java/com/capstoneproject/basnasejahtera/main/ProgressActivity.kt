package com.capstoneproject.basnasejahtera.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.databinding.ActivityProgressBinding
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory


class ProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val nomorRumah = intent.getStringExtra("nomorRumah")
        val updatedAt = intent.getStringExtra("updatedAt")
        val progress = intent.getIntExtra("progress", 0)
        val time = updatedAt?.dropLast(8)
        val cutTime = time?.drop(11)
        val cutDate = updatedAt?.dropLast(14)

        binding.apply {
            "Blok $nomorRumah".also { tvBlok.text = it }
            "Terakhir Update : $cutDate || $cutTime WIB".also { binding.tvLastUpdate.text = it }
            progressBar.progress = progress
            "$progress%".also { textViewProgress.text = it }
        }
    }
}