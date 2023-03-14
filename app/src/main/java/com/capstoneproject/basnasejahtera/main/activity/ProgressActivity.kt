package com.capstoneproject.basnasejahtera.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.BuildConfig
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityProgressBinding
import com.capstoneproject.basnasejahtera.main.viewmodel.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import java.util.*

class ProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.progress)
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

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
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

        detailDataViewModel.dataRumah.observe(this) { data ->
            val nomorRumah = data.nomorRumah
            val persentaseProgress = data.progressPembangunan
            val detailProgress = data.detailsProgressPembangunan
            val photo = data.imageProgressPembangunan
            val img = BuildConfig.BASE_URL + "uploads/$photo"
            val dataUpdate = data.updatedAt
            val cutDate = dataUpdate?.dropLast(14)

            binding.apply {
                "Blok $nomorRumah".also { tvBlok.text = it }
                "Terakhir Update : $cutDate".also { binding.tvLastUpdate.text = it }
                progressBar.progress = persentaseProgress
                "$persentaseProgress%".also { textViewProgress.text = it }
                "Detail Progress Pembangunan : $detailProgress".also { tvDetailProgress.text = it }
                Glide.with(this@ProgressActivity)
                    .load(img)
                    .placeholder(R.drawable.progress)
                    .error(R.drawable.progress)
                    .into(ivProgress)
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