package com.capstoneproject.basnasejahtera.admin.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.admin.activity.KelolaAkunAdminActivity
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataKonsumenBinding
import com.capstoneproject.basnasejahtera.model.SeluruhAkunItem

class ListDataAkunAdapter :
    RecyclerView.Adapter<ListDataAkunAdapter.ListViewHolder>() {
    private var listDataAkun = ArrayList<SeluruhAkunItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setlistDataAkun(data: List<SeluruhAkunItem>) {
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
        fun bind(dataAkun: SeluruhAkunItem) {
            val statusBooking = dataAkun.dataBooking?.statusBooking

            binding.apply {
                tvNama.text = dataAkun.nama
                tvEmail.text = dataAkun.email

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
                    val intent =
                        Intent(itemView.context, KelolaAkunAdminActivity::class.java)
                    intent.putExtra("idAkun", dataAkun.id)
                    intent.putExtra("role", dataAkun.role)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}