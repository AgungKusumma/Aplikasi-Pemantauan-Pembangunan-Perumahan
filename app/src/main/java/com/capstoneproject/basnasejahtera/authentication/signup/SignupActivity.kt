package com.capstoneproject.basnasejahtera.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.HomeAdminActivity
import com.capstoneproject.basnasejahtera.authentication.AuthenticationViewModel
import com.capstoneproject.basnasejahtera.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.new_account)
        actionbar?.setDisplayHomeAsUpEnabled(true)


        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        authenticationViewModel = AuthenticationViewModel.getInstance(this)
    }

    private fun setupAction() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val nik = binding.nikEditText.text.toString()
            val noHp = binding.noHPEditText.text.toString()
            val pekerjaan = binding.pekerjaanEditText.text.toString()
            val alamat = binding.alamatEditText.text.toString()

            when {
                name.isEmpty() -> {
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
                    authenticationViewModel.userRegister(
                        email, password, name, noHp,
                        "Konsumen", nik, pekerjaan, alamat)
                    authenticationViewModel.error.observe(this@SignupActivity) { event ->
                        event.getContentIfNotHandled()?.let { error ->
                            if (!error) {
                                Toast.makeText(this,
                                    getString(R.string.signup_success),
                                    Toast.LENGTH_LONG).show()
                                val intent = Intent(this, HomeAdminActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this,
                                    getString(R.string.signup_failed),
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}