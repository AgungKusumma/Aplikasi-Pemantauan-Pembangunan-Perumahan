package com.capstoneproject.basnasejahtera.konsumen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityHomeKonsumenBinding
import com.capstoneproject.basnasejahtera.main.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.adapter.ListHomeKonsumenAdapter
import com.capstoneproject.basnasejahtera.main.dataStore
import com.capstoneproject.basnasejahtera.main.detail.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.ItemDataKonsumenHome
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HomeKonsumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel
    private lateinit var adapter: ListHomeKonsumenAdapter
    private var list = ArrayList<ItemDataKonsumenHome>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKonsumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        showRecyclerList()
        setupAction()
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

    private fun setupDateandTime() {
        val date = SimpleDateFormat("E, dd MMMM yyyy")
        val time = SimpleDateFormat("hh:mm a")

        val currentDate = date.format(Date())
        val currentTime = time.format(Date())

        binding.tvDate.text = currentDate
        binding.tvTime.text = currentTime
    }

    private fun showRecyclerList() {
        list.addAll(listData)

        adapter = ListHomeKonsumenAdapter(list)

        binding.apply {
            rvItemHouse.layoutManager = GridLayoutManager(this@HomeKonsumenActivity, 2)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }
    }

    private val listData: ArrayList<ItemDataKonsumenHome>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val dataKonsumen = resources.getStringArray(R.array.data_home_konsumen)
            val listData = ArrayList<ItemDataKonsumenHome>()
            for (i in dataKonsumen.indices) {
                val data = ItemDataKonsumenHome(dataPhoto[i], dataKonsumen[i])
                listData.add(data)
            }
            return listData
        }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainViewModel.logout()
            Toast.makeText(this@HomeKonsumenActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        setupDateandTime()
    }
}