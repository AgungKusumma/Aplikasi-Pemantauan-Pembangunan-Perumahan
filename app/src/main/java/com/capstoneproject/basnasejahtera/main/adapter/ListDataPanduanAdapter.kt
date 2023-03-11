package com.capstoneproject.basnasejahtera.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataKonsumenBinding
import com.capstoneproject.basnasejahtera.model.ItemDataPanduan

class ListDataPanduanAdapter(private val listData: ArrayList<ItemDataPanduan>) :
    RecyclerView.Adapter<ListDataPanduanAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataKonsumenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(private val binding: ItemRowDataKonsumenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rumah: ItemDataPanduan) {
            val iv = rumah.photo
            val data = rumah.data
            val detailData = rumah.detailData

            binding.apply {
                Glide.with(itemView.context)
                    .load(iv)
                    .into(ivUser)

                tvNama.text = data
                tvEmail.text = detailData

                when (data) {
                    "Data Rumah Saya" -> {
                        ivUser.setBackgroundColor(Color.GREEN)
                    }
                    "Hubungi Admin" -> {
                        ivUser.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_100))
                    }
                    "Logout / Keluar Akun" -> {
                        ivUser.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.maroon))
                    }
                }
            }
        }
    }
}
