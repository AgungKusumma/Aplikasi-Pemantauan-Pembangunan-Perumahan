package com.capstoneproject.basnasejahtera.konsumen.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityPanduanBinding
import com.capstoneproject.basnasejahtera.konsumen.adapter.ListDataPanduanAdapter
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.ItemDataPanduan
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

class PanduanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPanduanBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListDataPanduanAdapter
    private var list = ArrayList<ItemDataPanduan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanduanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.panduan)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        showRecyclerList()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        val konsumen = getString(R.string.role_konsumen)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != konsumen) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun showRecyclerList() {
        list.addAll(listData)

        adapter = ListDataPanduanAdapter(list)

        binding.apply {
            rvItemHouse.layoutManager = LinearLayoutManager(this@PanduanActivity)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }
    }

    private val listData: ArrayList<ItemDataPanduan>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo_panduan)
            val dataPanduan = resources.getStringArray(R.array.data_panduan)
            val detailDataPanduan = resources.getStringArray(R.array.data_detail_panduan)
            val listData = ArrayList<ItemDataPanduan>()
            for (i in dataPanduan.indices) {
                val data = ItemDataPanduan(dataPhoto[i], dataPanduan[i], detailDataPanduan[i])
                listData.add(data)
            }
            return listData
        }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}