package com.capstoneproject.basnasejahtera.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.admin.KelolaAkunAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataKonsumenBinding
import com.capstoneproject.basnasejahtera.model.DataKonsumenResponseItem

class ListDataAkunAdapter :
    RecyclerView.Adapter<ListDataAkunAdapter.ListViewHolder>() {
    private var listDataAkun = ArrayList<DataKonsumenResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setlistDataAkun(data: List<DataKonsumenResponseItem>) {
        listDataAkun.clear()
        listDataAkun.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataKonsumenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listDataAkun[position])
    }

    override fun getItemCount(): Int = listDataAkun.size

    class ListViewHolder(private val binding: ItemRowDataKonsumenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataAkun: DataKonsumenResponseItem) {
            val statusBooking = dataAkun.dataBooking?.statusBooking

            binding.apply {
                tvNama.text = dataAkun.dataAkun?.nama
                tvEmail.text = dataAkun.dataAkun?.email

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
                    if (dataAkun.dataBooking == null) {
                        Toast.makeText(itemView.context,
                            "Hanya bisa mengubah data konsumen yang sudah memiliki rumah\n\nPilih Konsumen yang lain",
                            Toast.LENGTH_LONG).show()
                    } else {
                        val intent =
                            Intent(itemView.context, KelolaAkunAdminActivity::class.java)
                        intent.putExtra("idRumah", dataAkun.id)
                        intent.putExtra("idAkun", dataAkun.idAkun)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

}