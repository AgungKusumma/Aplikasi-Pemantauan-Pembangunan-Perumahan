package com.capstoneproject.basnasejahtera.admin.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.adapter.ListDataKonsumenAdapter
import com.capstoneproject.basnasejahtera.databinding.ActivityMainAdminBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainDataViewModel: MainDataViewModel
    private lateinit var adapter: ListDataKonsumenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        showRecyclerList()
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

        val admin = getString(R.string.role_admin)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != admin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun showRecyclerList() {
        adapter = ListDataKonsumenAdapter()

        binding.apply {
            rvItemKonsumen.layoutManager = GridLayoutManager(this@MainAdminActivity, 2)
            rvItemKonsumen.setHasFixedSize(true)
            rvItemKonsumen.adapter = adapter
        }

        mainDataViewModel.getAllDataKonsumen()

        mainDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainDataViewModel.dataKonsumen.observe(this) {
            adapter.setListDataKonsumen(it)
        }

        mainDataViewModel.error.observe(this) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (error) {
                    binding.rvItemKonsumen.adapter = null
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}