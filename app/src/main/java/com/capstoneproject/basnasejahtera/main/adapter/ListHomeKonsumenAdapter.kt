package com.capstoneproject.basnasejahtera.main.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.konsumen.DetailKonsumenActivity
import com.capstoneproject.basnasejahtera.konsumen.PanduanActivity
import com.capstoneproject.basnasejahtera.model.ItemData

class ListHomeKonsumenAdapter(private val listData: ArrayList<ItemData>) :
    RecyclerView.Adapter<ListHomeKonsumenAdapter.ListViewHolder>() {

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
                    "Rumah Saya" -> {
                        root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_200))
                    }
                    "Hubungi Admin" -> {
                        root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_100))
                    }
                    "Panduan" -> {
                        root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                            R.color.blue_50))
                    }
                }

                itemView.setOnClickListener {
                    when (data) {
                        "Rumah Saya" -> {
                            val intent =
                                Intent(itemView.context, DetailKonsumenActivity::class.java)
                            itemView.context.startActivity(intent)
                        }
                        "Hubungi Admin" -> {
                            val number = "6281273783202"
                            val url = "https://api.whatsapp.com/send?phone=$number"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            itemView.context.startActivity(intent)
                        }
                        "Panduan" -> {
                            val intent = Intent(itemView.context, PanduanActivity::class.java)
                            itemView.context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
