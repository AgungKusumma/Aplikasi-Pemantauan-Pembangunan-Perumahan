package com.capstoneproject.basnasejahtera.authentication.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.HomeAdminActivity
import com.capstoneproject.basnasejahtera.authentication.AuthenticationViewModel
import com.capstoneproject.basnasejahtera.databinding.ActivitySignupBinding
import com.capstoneproject.basnasejahtera.model.UserModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]

        authenticationViewModel = AuthenticationViewModel.getInstance(this)
    }

    private fun setupAction() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val nik = binding.nikEditText.text.toString()
            val noHp = binding.noHPEditText.text.toString()
            val alamat = binding.alamatEditText.text.toString()
            val pekerjaan = binding.pekerjaanEditText.text.toString()

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
                alamat.isEmpty() -> {
                    binding.alamatEditTextLayout.error = "Masukkan Alamat"
                }
                pekerjaan.isEmpty() -> {
                    binding.pekerjaanEditTextLayout.error = "Masukkan Pekerjaan"
                }
                else -> {
                    signupViewModel.saveUser(UserModel(name, email, password, "role", false))

                    authenticationViewModel.userRegister(email,
                        password,
                        name,
                        noHp,
                        "Konsumen",
                        nik,
                        pekerjaan,
                        alamat)
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

}