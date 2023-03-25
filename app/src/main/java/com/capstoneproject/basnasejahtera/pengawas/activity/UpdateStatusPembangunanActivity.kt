package com.capstoneproject.basnasejahtera.pengawas.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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
import com.capstoneproject.basnasejahtera.main.viewmodel.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.UpdateStatusViewModel
import com.capstoneproject.basnasejahtera.model.DataIDRumah
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.utils.createCustomTempFile
import com.capstoneproject.basnasejahtera.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdateStatusPembangunanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateStatusPembangunanBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel
    private lateinit var currentPhotoPath: String
    private lateinit var rumah: DataIDRumah
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

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.update_progress_pembangunan)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        setupData()
        setupAction()
    }

    private fun setupViewModel() {
        val idRumah = intent.getIntExtra("idRumah", 0)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        detailDataViewModel = DetailDataViewModel.getInstance(this)
        updateStatusViewModel = UpdateStatusViewModel.getInstance(this)

        val pengawas = getString(R.string.role_pengawas)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != pengawas) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        if (idRumah != 0) {
            mainViewModel.saveIDRumah(idRumah)
        }

        mainViewModel.getIDRumah().observe(this) { rumah ->
            this.rumah = rumah
            rumah.id?.let { detailDataViewModel.getDataRumah(it) }
        }
    }

    private fun setupData() {
        detailDataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailDataViewModel.dataRumah.observe(this) { rumah ->
            val nomorRumah = rumah.nomorRumah
            val nama = rumah.dataAkunKonsumen?.nama
            val email = rumah.dataAkunKonsumen?.email
            val progressPembangunan = rumah.progressPembangunan
            val detailProgress = rumah.detailsProgressPembangunan

            binding.apply {
                "Nomor Rumah : $nomorRumah".also { tvNomorRumah.text = it }
                if (nama != null && email != null) {
                    tvNama.visibility = View.VISIBLE
                    tvEmail.visibility = View.VISIBLE
                    "Nama Pemilik Rumah : $nama".also { tvNama.text = it }
                    "Email Pemilik Rumah : $email".also { tvEmail.text = it }
                } else {
                    tvNama.visibility = View.GONE
                    tvEmail.visibility = View.GONE
                }
                "Progress Pembangunan saat ini : $progressPembangunan%".also {
                    tvProgress.text = it
                }
                tvDetailProgress.visibility = View.GONE
                if (detailProgress != null) {
                    tvDetailProgress.visibility = View.VISIBLE
                    "Detail Progress Pembangunan saat ini : $detailProgress".also {
                        tvDetailProgress.text = it
                    }
                }
            }
        }
    }

    private fun setupAction() {
        val persentaseProgress = intent.getIntExtra("persentaseProgress", 0)
        val detailProgress = intent.getStringExtra("detailProgress")

        binding.apply {
            if (persentaseProgress != 0 || detailProgress != null) {
                tvNewProgress.visibility = View.VISIBLE
                tvNewDetailProgress.visibility = View.VISIBLE
                cameraButton.visibility = View.VISIBLE
                progressButton.visibility = View.GONE
                saveButton.visibility = View.VISIBLE

                "Progress Pembangunan Terbaru : $persentaseProgress%".also {
                    tvNewProgress.text = it
                }
                "Detail Progress Pembangunan Terbaru : $detailProgress".also {
                    tvNewDetailProgress.text = it
                }
            } else {
                saveButton.visibility = View.GONE
            }

            progressButton.setOnClickListener {
                val intent =
                    Intent(this@UpdateStatusPembangunanActivity,
                        UpdateProgressPengawasActivity::class.java)
                startActivity(intent)
            }
            cameraButton.setOnClickListener { startTakePhoto() }
            saveButton.setOnClickListener {
                updateStatusPembangunan(
                    persentaseProgress.toString(), detailProgress.toString()
                )
            }
        }
    }

    private fun updateStatusPembangunan(persen: String, detail: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val persentaseProgress = persen.toRequestBody("text/plain".toMediaType())
            val detailProgress = detail.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            updateStatusViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            mainViewModel.getIDRumah().observe(this@UpdateStatusPembangunanActivity) { rumah ->
                updateStatusViewModel.newUpdateStatusPembangunan(
                    rumah.id!!, imageMultipart, persentaseProgress, detailProgress
                )
            }

            updateStatusViewModel.error.observe(this) { event ->
                event.getContentIfNotHandled()?.let { error ->
                    if (!error) {
                        Toast.makeText(this,
                            getString(R.string.success_update_status_pembangunan),
                            Toast.LENGTH_LONG).show()
                        val intent =
                            Intent(this@UpdateStatusPembangunanActivity,
                                HomePengawasActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this,
                            getString(R.string.update_failed),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }

            updateStatusViewModel.message.observe(this) { event ->
                event.getContentIfNotHandled()?.let {
                    val msg = getString(R.string.update_failed)
                    val msg1 = getString(R.string.network_error)
                    Toast.makeText(this, "$msg \n$msg1", Toast.LENGTH_LONG)
                        .show()
                    showLoading(false)
                }
            }
        } else {
            val msg = getString(R.string.empty_image)
            Toast.makeText(this,
                msg,
                Toast.LENGTH_SHORT).show()
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
            binding.cameraButton.visibility = View.GONE
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

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}