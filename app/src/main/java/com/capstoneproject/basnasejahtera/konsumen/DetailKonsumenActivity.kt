package com.capstoneproject.basnasejahtera.konsumen

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityDetailKonsumenBinding
import com.capstoneproject.basnasejahtera.main.ProgressActivity
import com.capstoneproject.basnasejahtera.main.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.dataStore
import com.capstoneproject.basnasejahtera.main.detail.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
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
        mainViewModel.getDataUserKonsumen().observe(this) {
            if (it.id != null) {
                detailDataViewModel.getDataRumahKonsumen(it.id)
            }
        }

        detailDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailDataViewModel.dataRumah.observe(this) { rumah ->
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val price = nf.format(rumah.harga)

            val nik = rumah.dataKonsumen?.nik
            val noTelp = rumah.dataKonsumen?.noTelp
            val pekerjaan = rumah.dataKonsumen?.pekerjaan
            val alamat = rumah.dataKonsumen?.alamat

            binding.apply {
                "Blok ${rumah.nomorRumah}".also { tvBlok.text = it }
                "Tipe Rumah ${rumah.tipeRumah}".also { tvName.text = it }
                "Harga Rumah : Rp. $price".also { tvPrice.text = it }
                "Progress Pembangunan : ${rumah.progressPembangunan}%".also { tvProgress.text = it }
                "NIK : $nik".also { tvNIK.text = it }
                "No Telp : $noTelp".also { tvTelp.text = it }
                "Pekerjaan : $pekerjaan".also { tvPekerjaan.text = it }
                "Alamat : $alamat".also { tvAlamat.text = it }

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

                progressBtn.setOnClickListener {
                    val intent = Intent(this@DetailKonsumenActivity, ProgressActivity::class.java)
                    intent.putExtra("nomorRumah", rumah.nomorRumah)
                    intent.putExtra("updatedAt", rumah.updatedAt)
                    intent.putExtra("progress", rumah.progressPembangunan)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
