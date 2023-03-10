package com.capstoneproject.basnasejahtera.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityHomeKonsumenBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.adapter.ListHomeAdminAdapter
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.ItemData
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListHomeAdminAdapter
    private var list = ArrayList<ItemData>()

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

        val admin = getString(R.string.role_admin)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != admin) {
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

        adapter = ListHomeAdminAdapter(list)

        binding.apply {
            rvItemHouse.layoutManager = GridLayoutManager(this@HomeAdminActivity, 2)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }
    }

    private val listData: ArrayList<ItemData>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo_admin)
            val dataKonsumen = resources.getStringArray(R.array.data_home_admin)
            val listData = ArrayList<ItemData>()
            for (i in dataKonsumen.indices) {
                val data = ItemData(dataPhoto[i], dataKonsumen[i])
                listData.add(data)
            }
            return listData
        }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainViewModel.logout()
            Toast.makeText(this@HomeAdminActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        setupDateandTime()
    }
}