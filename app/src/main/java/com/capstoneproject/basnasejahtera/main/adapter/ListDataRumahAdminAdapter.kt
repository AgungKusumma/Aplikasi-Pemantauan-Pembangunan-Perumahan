package com.capstoneproject.basnasejahtera.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.admin.UpdateBookingAdminActivity
import com.capstoneproject.basnasejahtera.admin.UpdateRumahAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.model.DataRumahResponseItem

class ListDataRumahAdminAdapter :
    RecyclerView.Adapter<ListDataRumahAdminAdapter.ListViewHolder>() {
    private var listDataRumahAdmin = ArrayList<DataRumahResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListRumahAdmin(data: List<DataRumahResponseItem>) {
        listDataRumahAdmin.clear()
        listDataRumahAdmin.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listDataRumahAdmin[position])
    }

    override fun getItemCount(): Int = listDataRumahAdmin.size

    class ListViewHolder(private val binding: ItemRowDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataRumah: DataRumahResponseItem) {
            val statusRumah = dataRumah.statusRumah

            binding.apply {
                tvItemBlok.text = dataRumah.nomorRumah

                when (statusRumah) {
                    "terjual" -> {
                        binding.root.setBackgroundColor(Color.GREEN)
                    }
                    "di booking" -> {
                        binding.root.setBackgroundColor(Color.YELLOW)
                    }
                    "belum terjual" -> {
                        binding.root.setBackgroundColor(Color.WHITE)
                    }
                }

                itemView.setOnClickListener {
                    if (statusRumah == "terjual") {
                        Toast.makeText(itemView.context,
                            "Rumah sudah terjual\nPilih Rumah yang lain",
                            Toast.LENGTH_LONG).show()
                    } else if (dataRumah.dataBooking != null && dataRumah.dataBooking.statusBooking != "belum terjual") {
                        val intent =
                            Intent(itemView.context, UpdateRumahAdminActivity::class.java)
                        intent.putExtra("idRumah", dataRumah.id)
                        intent.putExtra("idKonsumen", dataRumah.dataBooking.idKonsumen)
                        itemView.context.startActivity(intent)
                    } else {
                        val intent =
                            Intent(itemView.context,
                                UpdateBookingAdminActivity::class.java)
                        intent.putExtra("idRumah", dataRumah.id)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

}