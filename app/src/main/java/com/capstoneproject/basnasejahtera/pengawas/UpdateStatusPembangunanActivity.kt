package com.capstoneproject.basnasejahtera.pengawas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityUpdateStatusPembangunanBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.UpdateStatusViewModel
import com.capstoneproject.basnasejahtera.model.DataStatus
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.utils.createCustomTempFile
import java.io.File

class UpdateStatusPembangunanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateStatusPembangunanBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

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

        updateStatusViewModel = UpdateStatusViewModel.getInstance(this)

        val pengawas = getString(R.string.role_pengawas)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pengawas) {
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
            cameraButton.setOnClickListener { startTakePhoto() }

            saveButton.setOnClickListener {
                val progress1 = progress.toString().toInt()
                val data = DataStatus(progress1)

                updateStatusViewModel.updateStatusPembangunan(idRumah, data)
                updateStatusViewModel.error.observe(this@UpdateStatusPembangunanActivity) { event ->
                    event.getContentIfNotHandled()?.let { error ->
                        if (!error) {
                            updateStatusViewModel.data.observe(this@UpdateStatusPembangunanActivity) { event ->
                                event.getContentIfNotHandled()?.let {
                                    Toast.makeText(this@UpdateStatusPembangunanActivity,
                                        getString(R.string.success_update_status_pembangunan),
                                        Toast.LENGTH_LONG).show()
                                    val intent =
                                        Intent(this@UpdateStatusPembangunanActivity,
                                            HomePengawasActivity::class.java)
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

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UpdateStatusPembangunanActivity,
                "com.capstoneproject.basnasejahtera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImage.setImageBitmap(result)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}