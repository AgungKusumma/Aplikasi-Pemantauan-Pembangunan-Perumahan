package com.capstoneproject.basnasejahtera.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityMainBinding
import com.capstoneproject.basnasejahtera.main.adapter.ListDataAdapter
import com.capstoneproject.basnasejahtera.model.ItemData
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvItem: RecyclerView
    private var list = ArrayList<ItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
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

        val pegawai = getString(R.string.role_pegawai)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pegawai) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun showRecyclerList() {
        rvItem = binding.rvItemHouse
        rvItem.setHasFixedSize(true)

        list.addAll(listMenu)

        rvItem.layoutManager = GridLayoutManager(this, 2)

        val listMenuAdapter = ListDataAdapter(list)
        rvItem.adapter = listMenuAdapter
    }

    private val listMenu: ArrayList<ItemData>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val dataId = resources.getStringArray(R.array.id_home)
            val dataInfo = resources.getStringArray(R.array.info_item)
            val dataNama = resources.getStringArray(R.array.data_name)
            val dataStatus = resources.getIntArray(R.array.data_status)
            val listMenu = ArrayList<ItemData>()
            for (i in dataId.indices) {
                val menu = ItemData(dataPhoto[i], dataId[i], dataInfo[i], dataNama[i], dataStatus[i])
                listMenu.add(menu)
            }
            return listMenu
        }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainViewModel.logout()
            Toast.makeText(this@MainActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }
}