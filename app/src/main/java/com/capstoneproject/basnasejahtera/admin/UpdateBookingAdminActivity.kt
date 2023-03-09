package com.capstoneproject.basnasejahtera.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityUpdateBookingAdminBinding
import com.capstoneproject.basnasejahtera.main.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.pengawas.UpdateStatusViewModel

class UpdateBookingAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBookingAdminBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookingAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupData()
        setupAction()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

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
        val nomorRumah = intent.getStringExtra("nomorRumah")
        val statusBooking = intent.getStringExtra("statusBooking")
        val tanggalBooking = intent.getStringExtra("tanggalBooking")

        "Nomor Rumah : $nomorRumah".also { binding.tvNomorRumah.text = it }
        "Nama Konsumen : $nomorRumah".also { binding.tvNama.text = it }
        "Status Rumah : $nomorRumah".also { binding.tvStatusBooking.text = it }
    }

    private fun setupAction() {
        val idRumah = intent.getIntExtra("idRumah", 0)
        val idKonsumen = intent.getIntExtra("idKonsumen", 0)
//        val progress = binding.statusPembangunanEditText.text

        binding.apply {
            saveButton.setOnClickListener {
//                val data = DataUpdateBooking(idKonsumen,
//                    newStatusBooking.toString(),
//                    nominalBooking!!,
//                    tanggalBooking!!)

//                updateStatusViewModel.updateStatusBooking(idRumah, data)
                updateStatusViewModel.error.observe(this@UpdateBookingAdminActivity) { event ->
                    event.getContentIfNotHandled()?.let { error ->
                        if (!error) {
                            updateStatusViewModel.data.observe(this@UpdateBookingAdminActivity) { event ->
                                event.getContentIfNotHandled()?.let {
                                    Toast.makeText(this@UpdateBookingAdminActivity,
                                        getString(R.string.success_update_status_pembangunan),
                                        Toast.LENGTH_LONG).show()
                                    val intent =
                                        Intent(this@UpdateBookingAdminActivity,
                                            HomeAdminActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(this@UpdateBookingAdminActivity,
                                getString(R.string.update_failed),
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}