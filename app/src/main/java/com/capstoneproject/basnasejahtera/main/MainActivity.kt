package com.capstoneproject.basnasejahtera.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityMainBinding
import com.capstoneproject.basnasejahtera.main.adapter.ListDataRumahAdapter
import com.capstoneproject.basnasejahtera.main.viewmodel.MainDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainDataViewModel: MainDataViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListDataRumahAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupListData()
        showRecyclerList()
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

        mainDataViewModel = MainDataViewModel.getInstance(this)

        val pegawai = getString(R.string.role_pegawai)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pegawai) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupListData() {
        mainViewModel.getUser().observe(this) {
            mainDataViewModel.getDataRumah()
        }
    }

    private fun showRecyclerList() {
        adapter = ListDataRumahAdapter()

        binding.apply {
            rvItemHouse.layoutManager = GridLayoutManager(this@MainActivity, 2)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }

        mainDataViewModel.dataRumah.observe(this) {
            adapter.setListOrder(it)
        }

        mainDataViewModel.error.observe(this) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (error) {
                    binding.rvItemHouse.adapter = null
                }
            }
        }
    }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainViewModel.logout()
            Toast.makeText(this@MainActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }
}