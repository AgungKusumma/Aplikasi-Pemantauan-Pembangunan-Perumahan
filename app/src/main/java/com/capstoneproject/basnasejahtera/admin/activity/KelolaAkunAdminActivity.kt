package com.capstoneproject.basnasejahtera.admin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityKelolaAkunKonsumenBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.UpdateStatusViewModel
import com.capstoneproject.basnasejahtera.model.DataUpdateAkun
import com.capstoneproject.basnasejahtera.model.DataUpdateAkunAdmin
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

class KelolaAkunAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKelolaAkunKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainDataViewModel: MainDataViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaAkunKonsumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.manage_account)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainDataViewModel = MainDataViewModel.getInstance(this)
        updateStatusViewModel = UpdateStatusViewModel.getInstance(this)

        val admin = getString(R.string.role_admin)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != admin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        val idAkun = intent.getIntExtra("idAkun", 0)
        val role = intent.getStringExtra("role")

        val konsumen = getString(R.string.role_konsumen)
        val nameEt = binding.nameEditText
        val emailEt = binding.emailEditText
        val passwordEt = binding.passwordEditText
        val nikEt = binding.nikEditText
        val noHpEt = binding.noHPEditText
        val pekerjaanEt = binding.pekerjaanEditText
        val alamatEt = binding.alamatEditText

        role?.let { checkRole(it) }

        mainDataViewModel.getDetailDataAkun(idAkun)

        mainDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainDataViewModel.detailDataAkun.observe(this) { data ->
            val nama = data.data?.akun?.nama
            val email = data.data?.akun?.email
            val password = data.data?.akun?.kataSandi
            val nik = data.data?.akun?.dataKonsumen?.nik
            val noTelp = data.data?.akun?.dataKonsumen?.noTelp
            val pekerjaan = data.data?.akun?.dataKonsumen?.pekerjaan
            val alamat = data.data?.akun?.dataKonsumen?.alamat

            nameEt.setText(nama)
            emailEt.setText(email)
            passwordEt.setText(password)
            nikEt.setText(nik)
            noHpEt.setText(noTelp)
            pekerjaanEt.setText(pekerjaan)
            alamatEt.setText(alamat)
        }

        binding.saveButton.setOnClickListener {
            if (role == konsumen) {
                updateDataAkun(idAkun)
            } else {
                updateDataAkunAdmin(idAkun)
            }
        }
    }

    private fun checkRole(role: String) {
        val konsumen = getString(R.string.role_konsumen)
        binding.apply {
            if (role == konsumen) {
                nikEditTextLayout.visibility = View.VISIBLE
                noHPEditTextLayout.visibility = View.VISIBLE
                pekerjaanEditTextLayout.visibility = View.VISIBLE
                alamatEditTextLayout.visibility = View.VISIBLE
            } else {
                nikEditTextLayout.visibility = View.GONE
                noHPEditTextLayout.visibility = View.GONE
                pekerjaanEditTextLayout.visibility = View.GONE
                alamatEditTextLayout.visibility = View.GONE
            }
        }
    }

    private fun updateDataAkun(idAkun: Int) {
        binding.apply {
            val nama = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val nik = nikEditText.text.toString()
            val noHp = noHPEditText.text.toString()
            val pekerjaan = pekerjaanEditText.text.toString()
            val alamat = alamatEditText.text.toString()

            when {
                nama.isEmpty() -> {
                    nameEditTextLayout.error = "Masukkan Nama"
                }
                email.isEmpty() -> {
                    emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    passwordEditTextLayout.error = "Masukkan password"
                }
                nik.isEmpty() -> {
                    nikEditTextLayout.error = "Masukkan NIK"
                }
                noHp.isEmpty() -> {
                    noHPEditTextLayout.error = "Masukkan No HP"
                }
                pekerjaan.isEmpty() -> {
                    pekerjaanEditTextLayout.error = "Masukkan Pekerjaan"
                }
                alamat.isEmpty() -> {
                    alamatEditTextLayout.error = "Masukkan Alamat"
                }
                else -> {
                    val data = DataUpdateAkun(
                        email, password, nama, noHp,
                        nik, pekerjaan, alamat)

                    updateStatusViewModel.updateDataAkun(idAkun, data)
                    getResponseUpdate()
                }
            }
        }
    }

    private fun updateDataAkunAdmin(idAkun: Int) {
        binding.apply {
            val nama = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            when {
                nama.isEmpty() -> {
                    nameEditTextLayout.error = "Masukkan Nama"
                }
                email.isEmpty() -> {
                    emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    val data = DataUpdateAkunAdmin(
                        email, password, nama)

                    updateStatusViewModel.updateDataAkunAdmin(idAkun, data)
                    getResponseUpdate()
                }
            }
        }
    }

    private fun getResponseUpdate() {
        updateStatusViewModel.error.observe(this@KelolaAkunAdminActivity) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (!error) {
                    updateStatusViewModel.data.observe(this@KelolaAkunAdminActivity) { event ->
                        event.getContentIfNotHandled()?.let {
                            Toast.makeText(this@KelolaAkunAdminActivity,
                                getString(R.string.success_update_status_pembangunan),
                                Toast.LENGTH_LONG).show()
                            val intent = Intent(this@KelolaAkunAdminActivity,
                                HomeAdminActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this@KelolaAkunAdminActivity,
                        getString(R.string.update_failed),
                        Toast.LENGTH_LONG).show()
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