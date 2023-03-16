package com.capstoneproject.basnasejahtera.admin.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.admin.activity.UpdateBookingAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataKonsumenBinding
import com.capstoneproject.basnasejahtera.model.DataKonsumenResponseItem

class ListDataKonsumenAdapter :
    RecyclerView.Adapter<ListDataKonsumenAdapter.ListViewHolder>() {
    private var listdataKonsumen = ArrayList<DataKonsumenResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListDataKonsumen(data: List<DataKonsumenResponseItem>) {
        listdataKonsumen.clear()
        listdataKonsumen.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataKonsumenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listdataKonsumen[position])
    }

    override fun getItemCount(): Int = listdataKonsumen.size

    class ListViewHolder(private val binding: ItemRowDataKonsumenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataKonsumen: DataKonsumenResponseItem) {
            val statusBooking = dataKonsumen.dataBooking?.statusBooking

            binding.apply {
                tvNama.text = dataKonsumen.dataAkun?.nama
                tvEmail.text = dataKonsumen.dataAkun?.email

                when (statusBooking) {
                    "terjual" -> {
                        root.setBackgroundColor(Color.GREEN)
                    }
                    "di booking" -> {
                        root.setBackgroundColor(Color.YELLOW)
                    }
                    else -> {
                        root.setBackgroundColor(Color.WHITE)
                    }
                }

                itemView.setOnClickListener {
                    when (statusBooking) {
                        "terjual" -> {
                            Toast.makeText(itemView.context,
                                "Konsumen sudah membeli rumah\nPilih Konsumen yang lain",
                                Toast.LENGTH_LONG).show()
                        }
                        "di booking" -> {
                            Toast.makeText(itemView.context,
                                "Konsumen sudah booking rumah\nPilih Konsumen yang lain",
                                Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            val intent =
                                Intent(itemView.context, UpdateBookingAdminActivity::class.java)
                            intent.putExtra("idKonsumen", dataKonsumen.id)
                            intent.putExtra("namaKonsumen", dataKonsumen.dataAkun?.nama)
                            intent.putExtra("emailKonsumen", dataKonsumen.dataAkun?.email)
                            itemView.context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }

}