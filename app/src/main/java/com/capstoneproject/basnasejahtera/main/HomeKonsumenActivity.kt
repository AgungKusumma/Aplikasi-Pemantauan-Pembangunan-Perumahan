package com.capstoneproject.basnasejahtera.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityHomeKonsumenBinding
import com.capstoneproject.basnasejahtera.konsumen.DetailKonsumenActivity
import com.capstoneproject.basnasejahtera.main.detail.DetailDataViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.util.*

class HomeKonsumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKonsumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupData()
        setupAction()
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

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        detailDataViewModel = DetailDataViewModel.getInstance(this)

        val konsumen = getString(R.string.role_konsumen)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != konsumen) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        binding.btnRumahSaya.setOnClickListener {
            mainViewModel.getDataUserKonsumen().observe(this) {

                val intent = Intent(this@HomeKonsumenActivity,
                    DetailKonsumenActivity::class.java)
                intent.putExtra("idKonsumen", it.id)
                startActivity(intent)
            }
        }
    }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainViewModel.logout()
            Toast.makeText(this@HomeKonsumenActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }
}