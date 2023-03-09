package com.capstoneproject.basnasejahtera.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityUpdateRumahAdminBinding
import com.capstoneproject.basnasejahtera.main.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.dataStore
import com.capstoneproject.basnasejahtera.main.detail.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.DataUpdateBooking
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.pengawas.UpdateStatusViewModel

class UpdateRumahAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateRumahAdminBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRumahAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        detailDataViewModel = DetailDataViewModel.getInstance(this)
        updateStatusViewModel = UpdateStatusViewModel.getInstance(this)

        val admin = getString(R.string.role_admin)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != admin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val idRumah = intent.getIntExtra("idRumah", 0)
        val idKonsumen = intent.getIntExtra("idKonsumen", 0)

        detailDataViewModel.getDataRumah(idRumah)

        detailDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailDataViewModel.dataRumah.observe(this) { rumah ->
            val nomorRumah = rumah.nomorRumah
            val statusBooking = rumah.statusRumah
            val namaKonsumen = rumah.dataAkunKonsumen?.nama
            val nominalBooking = rumah.dataBooking?.nominalBooking
            val tanggalBooking = rumah.dataBooking?.tanggalBooking

            binding.apply {
                var newStatusBooking = tvNewStatus.text

                "Nomor Rumah : $nomorRumah".also { tvNomorRumah.text = it }
                "Nama Konsumen : $namaKonsumen".also { tvNama.text = it }
                "Status Rumah : $statusBooking".also { tvStatusBooking.text = it }
                "Tanggal Booking : $tanggalBooking".also { tvTanggalBooking.text = it }
                nominalBookingEditText.setText(nominalBooking.toString())

                rgStatus.setOnCheckedChangeListener { _, i ->
                    val rb: RadioButton = findViewById(i)
                    val newStatus = rb.text.toString()

                    newStatusBooking = newStatus
                }

                saveButton.setOnClickListener {
                    if (newStatusBooking.isEmpty() || newStatusBooking == statusBooking) {
                        Toast.makeText(this@UpdateRumahAdminActivity,
                            getString(R.string.select_new_status_rumah),
                            Toast.LENGTH_LONG).show()
                    } else {
                        val data = DataUpdateBooking(idKonsumen,
                            newStatusBooking.toString(),
                            nominalBooking!!,
                            tanggalBooking!!)

                        updateStatusViewModel.updateStatusBooking(idRumah, data)
                        updateStatusViewModel.error.observe(this@UpdateRumahAdminActivity) { event ->
                            event.getContentIfNotHandled()?.let { error ->
                                if (!error) {
                                    updateStatusViewModel.data.observe(this@UpdateRumahAdminActivity) { event ->
                                        event.getContentIfNotHandled()?.let {
                                            Toast.makeText(this@UpdateRumahAdminActivity,
                                                getString(R.string.success_update_status_pembangunan),
                                                Toast.LENGTH_LONG).show()
                                            val intent =
                                                Intent(this@UpdateRumahAdminActivity,
                                                    HomeAdminActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this@UpdateRumahAdminActivity,
                                        getString(R.string.update_failed),
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}