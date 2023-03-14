package com.capstoneproject.basnasejahtera.main.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityDetailBinding
import com.capstoneproject.basnasejahtera.main.viewmodel.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.text.NumberFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.detail_rumah)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        detailDataViewModel = DetailDataViewModel.getInstance(this)

        val pegawai = getString(R.string.role_pegawai)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pegawai) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val idRumah = intent.getIntExtra("idRumah", 0)

        detailDataViewModel.getDataRumah(idRumah)

        detailDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailDataViewModel.dataRumah.observe(this) { rumah ->
            val localeID = Locale("in", "ID")
            val nf: NumberFormat = NumberFormat.getInstance(localeID)
            val price = nf.format(rumah.harga)

            val nama = rumah.dataAkunKonsumen?.nama
            val nik = rumah.dataKonsumen?.nik
            val noTelp = rumah.dataKonsumen?.noTelp
            val pekerjaan = rumah.dataKonsumen?.pekerjaan
            val alamat = rumah.dataKonsumen?.alamat
            val nominalBooking = rumah.dataBooking?.nominalBooking
            val tglBooking = rumah.dataBooking?.tanggalBooking

            binding.apply {
                "Blok ${rumah.nomorRumah}".also { tvBlok.text = it }
                "Tipe Rumah : ${rumah.tipeRumah}".also { tvTipeRumah.text = it }
                "Harga Rumah : Rp. $price".also { tvPrice.text = it }
                "Progress Pembangunan : ${rumah.progressPembangunan}%".also { tvProgress.text = it }
                "Status Rumah : ${rumah.statusRumah}".also { tvStatusBooking.text = it }

                if (rumah.dataBooking != null) {
                    val bookingPrice = nf.format(nominalBooking)
                    tvNominal.isVisible = true
                    tvTanggal.isVisible = true

                    "Nominal Booking : Rp.$bookingPrice".also { tvNominal.text = it }
                    "Tanggal Booking : $tglBooking".also { tvTanggal.text = it }
                } else {
                    tvNominal.isVisible = false
                    tvTanggal.isVisible = false
                }

                if (rumah.dataKonsumen != null) {
                    tvNama.isVisible = true
                    tvNIK.isVisible = true
                    tvTelp.isVisible = true
                    tvPekerjaan.isVisible = true
                    tvAlamat.isVisible = true

                    "Nama Pemilik : $nama".also { tvNama.text = it }
                    "NIK : $nik".also { tvNIK.text = it }
                    "No Telp : $noTelp".also { tvTelp.text = it }
                    "Pekerjaan : $pekerjaan".also { tvPekerjaan.text = it }
                    "Alamat : $alamat".also { tvAlamat.text = it }
                } else {
                    tvNama.isVisible = false
                    tvNIK.isVisible = false
                    tvTelp.isVisible = false
                    tvPekerjaan.isVisible = false
                    tvAlamat.isVisible = false
                }

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
                    val intent = Intent(this@DetailActivity, ProgressActivity::class.java)
                    intent.putExtra("idRumah", idRumah)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}