package com.capstoneproject.basnasejahtera.konsumen

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityDetailKonsumenBinding
import com.capstoneproject.basnasejahtera.main.MainViewModel
import com.capstoneproject.basnasejahtera.main.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.dataStore
import com.capstoneproject.basnasejahtera.main.detail.DetailDataViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.NumberFormat
import java.util.*

class DetailKonsumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKonsumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupData()
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

        detailDataViewModel = DetailDataViewModel.getInstance(this)

        val konsumen = getString(R.string.role_konsumen)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != konsumen) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val idKonsumen = intent.getIntExtra("idKonsumen", 0)

        mainViewModel.getUser().observe(this) {
            detailDataViewModel.getDataRumahKonsumen(idKonsumen)
        }

        detailDataViewModel.dataRumah.observe(this) { rumah ->
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val price = nf.format(rumah.harga)

            binding.apply {
                "Blok ${rumah.nomorRumah}".also { tvBlok.text = it }
                "Tipe Rumah ${rumah.tipeRumah}".also { tvName.text = it }
                "Harga Rumah : Rp. $price".also { tvPrice.text = it }
                "Progress Pembangunan : ${rumah.progressPembangunan}%".also { tvProgress.text = it }
//                "NIK : ${rumah.nik}".also { tvNIK.text = it }
//                "No Telp : ${rumah.noTelp}".also { tvTelp.text = it }
//                "Pekerjaan : ${rumah.pekerjaan}".also { tvPekerjaan.text = it }
//                "Alamat : ${rumah.alamat}".also { tvAlamat.text = it }
                progressBar.progress = rumah.progressPembangunan
                "${rumah.progressPembangunan}%".also { textViewProgress.text = it }

                when (rumah.statusRumah) {
                    "terjual" -> {
                        binding.ivHouse.setBackgroundColor(Color.GREEN)
                    }
                    "di booking" -> {
                        binding.ivHouse.setBackgroundColor(Color.YELLOW)
                    }
                    "belum terjual" -> {
                        binding.ivHouse.setBackgroundColor(Color.WHITE)
                    }
                }
            }
        }
    }
}
