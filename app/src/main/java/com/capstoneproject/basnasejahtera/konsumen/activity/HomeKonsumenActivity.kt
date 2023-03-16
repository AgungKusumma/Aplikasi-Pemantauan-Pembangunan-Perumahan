package com.capstoneproject.basnasejahtera.konsumen.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityHomeKonsumenBinding
import com.capstoneproject.basnasejahtera.konsumen.adapter.ListHomeKonsumenAdapter
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.ItemData
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HomeKonsumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListHomeKonsumenAdapter
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

        val konsumen = getString(R.string.role_konsumen)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != konsumen) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupDateandTime() {
        val localeID = Locale("in", "ID")
        val date = SimpleDateFormat("EEEE, dd MMMM yyyy", localeID)
        val time = SimpleDateFormat("HH:mm", localeID)

        val currentDate = date.format(Date())
        val currentTime = time.format(Date())

        binding.tvDate.text = currentDate
        "$currentTime WIB".also { binding.tvTime.text = it }
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

    private val listData: ArrayList<ItemData>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val dataKonsumen = resources.getStringArray(R.array.data_home_konsumen)
            val listData = ArrayList<ItemData>()
            for (i in dataKonsumen.indices) {
                val data = ItemData(dataPhoto[i], dataKonsumen[i])
                listData.add(data)
            }
            return listData
        }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle(getString(R.string.logout))
            alert.setMessage(getString(R.string.logout_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    mainViewModel.logout()
                    Toast.makeText(this,
                        getString(R.string.logout_success), Toast.LENGTH_LONG).show()
                }.setNegativeButton(getString(R.string.no), null)

            val alert1 = alert.create()
            alert1.show()
        }
    }

    override fun onResume() {
        super.onResume()
        setupDateandTime()
    }
}