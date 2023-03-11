package com.capstoneproject.basnasejahtera.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityHomeBinding
import com.capstoneproject.basnasejahtera.main.adapter.ListBlokAdapter
import com.capstoneproject.basnasejahtera.main.viewmodel.MainDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainDataViewModel: MainDataViewModel
    private lateinit var adapter: ListBlokAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupDateandTime()
        setupListData()
        showRecyclerList()
        setupAction()
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

    private fun setupDateandTime() {
        val localeID = Locale("in", "ID")
        val date = SimpleDateFormat("EEEE, dd MMMM yyyy", localeID)
        val time = SimpleDateFormat("HH:mm", localeID)

        val currentDate = date.format(Date())
        val currentTime = time.format(Date())

        binding.tvDate.text = currentDate
        "$currentTime WIB".also { binding.tvTime.text = it }
    }

    private fun setupListData() {
        mainDataViewModel.getBlok()
    }

    private fun showRecyclerList() {
        adapter = ListBlokAdapter()

        binding.apply {
            rvItemHouse.layoutManager = GridLayoutManager(this@HomeActivity, 2)
            rvItemHouse.setHasFixedSize(true)
            rvItemHouse.adapter = adapter
        }

        mainDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainDataViewModel.dataBlok.observe(this) {
            adapter.setListBlok(it)
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
            Toast.makeText(this@HomeActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        setupDateandTime()
    }
}