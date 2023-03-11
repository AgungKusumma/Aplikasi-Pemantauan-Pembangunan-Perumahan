package com.capstoneproject.basnasejahtera.konsumen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.HomeAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ActivityKelolaAkunKonsumenBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.UpdateStatusViewModel
import com.capstoneproject.basnasejahtera.model.DataUpdateAkun
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory


class KelolaAkunKonsumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKelolaAkunKonsumenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaAkunKonsumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
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

    private fun setupAction() {
        val idAkun = intent.getIntExtra("idAkun", 0)
        val nameEt = binding.nameEditText
        val emailEt = binding.emailEditText
        val passwordEt = binding.passwordEditText
        val nikEt = binding.nikEditText
        val noHpEt = binding.noHPEditText
        val pekerjaanEt = binding.pekerjaanEditText
        val alamatEt = binding.alamatEditText

        detailDataViewModel.getDataRumahKonsumen(idAkun)

        detailDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailDataViewModel.dataRumah.observe(this) { rumah ->
            val nama = rumah.dataAkunKonsumen?.nama
            val email = rumah.dataAkunKonsumen?.email
            val password = rumah.dataAkunKonsumen?.kataSandi
            val nik = rumah.dataKonsumen?.nik
            val noTelp = rumah.dataKonsumen?.noTelp
            val pekerjaan = rumah.dataKonsumen?.pekerjaan
            val alamat = rumah.dataKonsumen?.alamat

            nameEt.setText(nama)
            emailEt.setText(email)
            passwordEt.setText(password)
            nikEt.setText(nik)
            noHpEt.setText(noTelp)
            pekerjaanEt.setText(pekerjaan)
            alamatEt.setText(alamat)
        }

        binding.saveButton.setOnClickListener {
            val nama = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val nik = binding.nikEditText.text.toString()
            val noHp = binding.noHPEditText.text.toString()
            val pekerjaan = binding.pekerjaanEditText.text.toString()
            val alamat = binding.alamatEditText.text.toString()

            when {
                nama.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan Nama"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                nik.isEmpty() -> {
                    binding.nikEditTextLayout.error = "Masukkan NIK"
                }
                noHp.isEmpty() -> {
                    binding.noHPEditTextLayout.error = "Masukkan No HP"
                }
                pekerjaan.isEmpty() -> {
                    binding.pekerjaanEditTextLayout.error = "Masukkan Pekerjaan"
                }
                alamat.isEmpty() -> {
                    binding.alamatEditTextLayout.error = "Masukkan Alamat"
                }
                else -> {
                    val data = DataUpdateAkun(email, password, nama, noHp, nik, pekerjaan, alamat)

                    updateStatusViewModel.updateDataAkun(idAkun, data)
                    updateStatusViewModel.error.observe(this) { event ->
                        event.getContentIfNotHandled()?.let { error ->
                            if (!error) {
                                updateStatusViewModel.data.observe(this@KelolaAkunKonsumenActivity) { event ->
                                    event.getContentIfNotHandled()?.let {
                                        Toast.makeText(this,
                                            getString(R.string.success_update_status_pembangunan),
                                            Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, HomeAdminActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            } else {
                                Toast.makeText(this,
                                    getString(R.string.update_failed),
                                    Toast.LENGTH_LONG).show()
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