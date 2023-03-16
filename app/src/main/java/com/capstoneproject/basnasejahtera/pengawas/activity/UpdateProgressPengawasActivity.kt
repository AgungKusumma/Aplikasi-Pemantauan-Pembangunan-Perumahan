package com.capstoneproject.basnasejahtera.pengawas.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityUpdateProgressPengawasBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.ItemDataProgress
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.pengawas.adapter.ListDataProgressAdapter

class UpdateProgressPengawasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProgressPengawasBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListDataProgressAdapter
    private var list = ArrayList<ItemDataProgress>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProgressPengawasBinding.inflate(layoutInflater)
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

        val pengawas = getString(R.string.role_pengawas)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pengawas) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun showRecyclerList() {
        list.addAll(listData)

        adapter = ListDataProgressAdapter(list)

        binding.apply {
            rvItemHouse.layoutManager = LinearLayoutManager(this@UpdateProgressPengawasActivity)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }
    }

    private val listData: ArrayList<ItemDataProgress>
        get() {
            val dataPersentase = resources.getIntArray(R.array.persentase_progress)
            val dataDetailProgress = resources.getStringArray(R.array.detail_progress)
            val listData = ArrayList<ItemDataProgress>()
            for (i in dataPersentase.indices) {
                val data = ItemDataProgress(dataPersentase[i], dataDetailProgress[i])
                listData.add(data)
            }
            return listData
        }
}