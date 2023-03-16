package com.capstoneproject.basnasejahtera.authentication.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.activity.HomeAdminActivity
import com.capstoneproject.basnasejahtera.authentication.AuthenticationViewModel
import com.capstoneproject.basnasejahtera.databinding.ActivityLoginBinding
import com.capstoneproject.basnasejahtera.konsumen.activity.HomeKonsumenActivity
import com.capstoneproject.basnasejahtera.main.activity.HomeActivity
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.model.DataKonsumen
import com.capstoneproject.basnasejahtera.model.UserModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.pengawas.activity.HomePengawasActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var user: UserModel
    private lateinit var konsumen: DataKonsumen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        setupAccount()
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        authenticationViewModel = AuthenticationViewModel.getInstance(this)

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }

        loginViewModel.getDataUserKonsumen().observe(this) { konsumen ->
            this.konsumen = konsumen
        }
    }

    private fun setupAction() {
        val pegawai = getString(R.string.role_pegawai)
        val konsumen = getString(R.string.role_konsumen)
        val admin = getString(R.string.role_admin)
        val pengawas = getString(R.string.role_pengawas)

        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                when {
                    email.isEmpty() -> {
                        emailEditTextLayout.error = "Masukkan email"
                    }
                    password.isEmpty() -> {
                        passwordEditTextLayout.error = "Masukkan password"
                    }
                    else -> {
                        loginViewModel.login()

                        authenticationViewModel.isLoading.observe(this@LoginActivity) {
                            showLoading(it)
                        }
                        authenticationViewModel.userLogin(email, password)
                        authenticationViewModel.error.observe(this@LoginActivity) { event ->
                            event.getContentIfNotHandled()?.let { error ->
                                if (!error) {
                                    authenticationViewModel.user.observe(this@LoginActivity) { event ->
                                        event.getContentIfNotHandled()?.let {
                                            loginViewModel.saveData(it.role)
                                            loginViewModel.saveDataKonsumen(it.dataKonsumen?.id)
                                            when (it.role) {
                                                pegawai -> {
                                                    val intent = Intent(this@LoginActivity,
                                                        HomeActivity::class.java)
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                konsumen -> {
                                                    val intent = Intent(this@LoginActivity,
                                                        HomeKonsumenActivity::class.java)
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                admin -> {
                                                    val intent = Intent(this@LoginActivity,
                                                        HomeAdminActivity::class.java)
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                pengawas -> {
                                                    val intent = Intent(this@LoginActivity,
                                                        HomePengawasActivity::class.java)
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                else -> {
                                                    val intent = Intent(this@LoginActivity,
                                                        WelcomeActivity::class.java)
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }

                                            Toast.makeText(this@LoginActivity,
                                                getString(R.string.login_success),
                                                Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity,
                                        getString(R.string.login_failed),
                                        Toast.LENGTH_LONG).show()
                                    emailEditText.error =
                                        getString(R.string.wrong_password_and_email)
                                    passwordEditText.error =
                                        getString(R.string.wrong_password_and_email)
                                    emailEditText.requestFocus(1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAccount() {
        binding.apply {
            callAdminTextView.setOnClickListener {
                emailEditTextLayout.error = null
                passwordEditTextLayout.error = null
                emailEditText.apply { text?.clear() }
                passwordEditText.apply { text?.clear() }

                val number = "6281273783202"
                val url = "https://api.whatsapp.com/send?phone=$number"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}