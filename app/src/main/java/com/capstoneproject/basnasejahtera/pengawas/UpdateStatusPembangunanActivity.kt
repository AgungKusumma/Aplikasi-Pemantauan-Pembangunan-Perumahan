package com.capstoneproject.basnasejahtera.pengawas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.HomeAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ActivityUpdateStatusPembangunanBinding
import com.capstoneproject.basnasejahtera.main.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.DataStatus
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

class UpdateStatusPembangunanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateStatusPembangunanBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var updateStatusPembangunanViewModel: UpdateStatusPembangunanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateStatusPembangunanBinding.inflate(layoutInflater)
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

        updateStatusPembangunanViewModel = UpdateStatusPembangunanViewModel.getInstance(this)

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
        val progressPembangunan = intent.getIntExtra("progress", 0)

        "Nomor Rumah : $nomorRumah".also { binding.tvNomorRumah.text = it }
        "Progress Pembangunan saat ini : $progressPembangunan%".also {
            binding.tvProgress.text = it
        }
    }

    private fun setupAction() {
        val idRumah = intent.getIntExtra("idRumah", 0)
        val progress = binding.statusPembangunanEditText.text

        binding.apply {
            saveButton.setOnClickListener {
                val progress1 = progress.toString().toInt()
                val data = DataStatus(progress1)

                updateStatusPembangunanViewModel.updateStatusPembangunan(idRumah, data)
                updateStatusPembangunanViewModel.error.observe(this@UpdateStatusPembangunanActivity) { event ->
                    event.getContentIfNotHandled()?.let { error ->
                        if (!error) {
                            updateStatusPembangunanViewModel.data.observe(this@UpdateStatusPembangunanActivity) { event ->
                                event.getContentIfNotHandled()?.let {
                                    Toast.makeText(this@UpdateStatusPembangunanActivity,
                                        getString(R.string.success_update_status_pembangunan),
                                        Toast.LENGTH_LONG).show()
                                    val intent =
                                        Intent(this@UpdateStatusPembangunanActivity,
                                            HomeAdminActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            Toast.makeText(this@UpdateStatusPembangunanActivity,
                                getString(R.string.update_failed),
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}