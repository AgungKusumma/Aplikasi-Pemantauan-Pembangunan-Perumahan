package com.capstoneproject.basnasejahtera.pengawas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityHomePengawasBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.adapter.ListHomePengawasAdapter
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.ItemData
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HomePengawasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePengawasBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListHomePengawasAdapter
    private var list = ArrayList<ItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePengawasBinding.inflate(layoutInflater)
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

        val pengawas = getString(R.string.role_pengawas)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pengawas) {
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

        adapter = ListHomePengawasAdapter(list)

        binding.apply {
            rvItemHouse.layoutManager = GridLayoutManager(this@HomePengawasActivity, 2)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }
    }

    private val listData: ArrayList<ItemData>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo_pengawas)
            val dataKonsumen = resources.getStringArray(R.array.data_home_pengawas)
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
            Toast.makeText(this@HomePengawasActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        setupDateandTime()
    }
}