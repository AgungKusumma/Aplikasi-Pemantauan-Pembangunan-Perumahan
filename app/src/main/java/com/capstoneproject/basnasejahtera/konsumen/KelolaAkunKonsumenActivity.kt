package com.capstoneproject.basnasejahtera.konsumen

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ActivityKelolaAkunKonsumenBinding


class KelolaAkunKonsumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKelolaAkunKonsumenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaAkunKonsumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner = binding.spinnerNama
        val dataSpinner = arrayOf("agung", "kusuma")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, dataSpinner)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //onNothingSelected
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                //onItemSelected
            }
        }
    }

}