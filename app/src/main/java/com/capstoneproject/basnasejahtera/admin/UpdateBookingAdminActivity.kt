package com.capstoneproject.basnasejahtera.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityUpdateBookingAdminBinding
import com.capstoneproject.basnasejahtera.main.activity.WelcomeActivity
import com.capstoneproject.basnasejahtera.main.activity.dataStore
import com.capstoneproject.basnasejahtera.main.viewmodel.DetailDataViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.MainViewModel
import com.capstoneproject.basnasejahtera.main.viewmodel.UpdateStatusViewModel
import com.capstoneproject.basnasejahtera.model.DataIDRumah
import com.capstoneproject.basnasejahtera.model.DataUpdateBooking
import com.capstoneproject.basnasejahtera.model.UserPreference
import com.capstoneproject.basnasejahtera.model.ViewModelFactory
import com.capstoneproject.basnasejahtera.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class UpdateBookingAdminActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: ActivityUpdateBookingAdminBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailDataViewModel: DetailDataViewModel
    private lateinit var updateStatusViewModel: UpdateStatusViewModel
    private lateinit var rumah: DataIDRumah
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookingAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val admin = getString(R.string.role_admin)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != admin) {
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
            val statusBooking = rumah.statusRumah

            "Nomor Rumah : $nomorRumah".also { binding.tvNomorRumah.text = it }
            "Status Rumah : $statusBooking".also { binding.tvStatusBooking.text = it }
        }
    }

    private fun setupAction() {
        val idKonsumen = intent.getIntExtra("idKonsumen", 0)
        val namaKonsumen = intent.getStringExtra("namaKonsumen")
        val emailKonsumen = intent.getStringExtra("emailKonsumen")

        "Nama Konsumen : $namaKonsumen".also { binding.tvNama.text = it }
        "Email Konsumen : $emailKonsumen".also { binding.tvEmail.text = it }

        binding.apply {
            if (namaKonsumen != null) {
                selectKonsumenButton.visibility = View.GONE
            } else {
                selectKonsumenButton.visibility = View.VISIBLE
            }

            var newStatusBooking = tvNewStatus.text

            selectKonsumenButton.setOnClickListener {
                val intent =
                    Intent(this@UpdateBookingAdminActivity,
                        MainAdminActivity::class.java)
                startActivity(intent)
            }

            btnOnceDate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }

            rgStatus.setOnCheckedChangeListener { _, i ->
                val rb: RadioButton = findViewById(i)
                val newStatus = rb.text.toString()

                newStatusBooking = newStatus
            }

            saveButton.setOnClickListener {
                if (idKonsumen == 0) {
                    Toast.makeText(this@UpdateBookingAdminActivity,
                        getString(R.string.select_konsumen),
                        Toast.LENGTH_LONG).show()
                } else if (nominalBookingEditText.text.toString().trim().isEmpty()) {
                    Toast.makeText(this@UpdateBookingAdminActivity,
                        getString(R.string.booking_nominal_new),
                        Toast.LENGTH_LONG).show()
                } else if (newStatusBooking.isEmpty()) {
                    Toast.makeText(this@UpdateBookingAdminActivity,
                        getString(R.string.select_new_status_rumah),
                        Toast.LENGTH_LONG).show()
                } else if (!clicked) {
                    Toast.makeText(this@UpdateBookingAdminActivity,
                        getString(R.string.select_booking_date),
                        Toast.LENGTH_LONG).show()
                } else {
                    val tanggalBooking = tvTanggalBooking.text
                    val nominalBooking = nominalBookingEditText.text
                    val newNominal = nominalBooking.toString().toInt()
                    val data = DataUpdateBooking(idKonsumen,
                        newStatusBooking.toString(),
                        newNominal,
                        tanggalBooking.toString())

                    mainViewModel.getIDRumah().observe(this@UpdateBookingAdminActivity) { rumah ->
                        updateStatusViewModel.updateStatusBooking(rumah.id!!, data)
                    }
                    updateStatusViewModel.error.observe(this@UpdateBookingAdminActivity) { event ->
                        event.getContentIfNotHandled()?.let { error ->
                            if (!error) {
                                updateStatusViewModel.data.observe(this@UpdateBookingAdminActivity) { event ->
                                    event.getContentIfNotHandled()?.let {
                                        Toast.makeText(this@UpdateBookingAdminActivity,
                                            getString(R.string.success_update_status_pembangunan),
                                            Toast.LENGTH_LONG).show()
                                        val intent =
                                            Intent(this@UpdateBookingAdminActivity,
                                                HomeAdminActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            } else {
                                Toast.makeText(this@UpdateBookingAdminActivity,
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

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvOnceDate.text = dateFormat.format(calendar.time)
        binding.tvTanggalBooking.text = dateFormat.format(calendar.time)
        clicked = true
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }
}