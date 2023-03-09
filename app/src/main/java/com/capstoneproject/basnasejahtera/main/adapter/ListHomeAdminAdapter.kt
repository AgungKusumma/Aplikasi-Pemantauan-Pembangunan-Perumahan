package com.capstoneproject.basnasejahtera.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.admin.DataRumahAdminActivity
import com.capstoneproject.basnasejahtera.authentication.signup.SignupActivity
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.konsumen.KelolaAkunKonsumenActivity
import com.capstoneproject.basnasejahtera.model.ItemData

class ListHomeAdminAdapter(private val listData: ArrayList<ItemData>) :
    RecyclerView.Adapter<ListHomeAdminAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(private val binding: ItemRowDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rumah: ItemData) {
            val data = rumah.data

            binding.apply {
                Glide.with(itemView.context)
                    .load(rumah.photo)
                    .into(ivHouse)

                tvItemBlok.text = data

                when (data) {
                    "Update Status Booking" -> {
                        binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_100))
                    }
                    "Tambah Akun Baru" -> {
                        binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_50))
                    }
                    "Kelola Akun Konsumen" -> {
                        binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_50))
                    }
                }

                itemView.setOnClickListener {
                    when (data) {
                        "Update Status Booking" -> {
                            val intent =
                                Intent(itemView.context, DataRumahAdminActivity::class.java)
                            itemView.context.startActivity(intent)
                        }
                        "Tambah Akun Baru" -> {
                            val intent =
                                Intent(itemView.context, SignupActivity::class.java)
                            itemView.context.startActivity(intent)
                        }
                        "Kelola Akun Konsumen" -> {
                            val intent =
                                Intent(itemView.context, KelolaAkunKonsumenActivity::class.java)
                            itemView.context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
