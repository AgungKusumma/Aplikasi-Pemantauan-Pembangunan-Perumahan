package com.capstoneproject.basnasejahtera

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.admin.HomeAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ActivitySplashScreenBinding
import com.capstoneproject.basnasejahtera.konsumen.HomeKonsumenActivity
import com.capstoneproject.basnasejahtera.main.*
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.pengawas.HomePengawasActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            setupViewModel()
        }, delayTime)//delay 3 seconds
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
        val konsumen = getString(R.string.role_konsumen)
        val admin = getString(R.string.role_admin)
        val pengawas = getString(R.string.role_pengawas)

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin && user.role == pegawai) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else if (user.isLogin && user.role == konsumen) {
                startActivity(Intent(this, HomeKonsumenActivity::class.java))
                finish()
            } else if (user.isLogin && user.role == admin) {
                startActivity(Intent(this, HomeAdminActivity::class.java))
                finish()
            } else if (user.isLogin && user.role == pengawas) {
                startActivity(Intent(this, HomePengawasActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    companion object {
        private const val delayTime: Long = 2000
    }
}