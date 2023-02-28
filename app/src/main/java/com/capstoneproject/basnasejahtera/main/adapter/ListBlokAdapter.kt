package com.capstoneproject.basnasejahtera.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.main.MainActivity
import com.capstoneproject.basnasejahtera.model.DataBlokRumahResponseItem
import java.util.*

class ListBlokAdapter : RecyclerView.Adapter<ListBlokAdapter.ListViewHolder>() {

    private var listBlok = ArrayList<DataBlokRumahResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListBlok(menu: List<DataBlokRumahResponseItem>) {
        listBlok.clear()
        listBlok.addAll(menu)
        listBlok.add(DataBlokRumahResponseItem(nama = "all"))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBlok[position])
    }

    override fun getItemCount(): Int = listBlok.size

    class ListViewHolder(private val binding: ItemRowDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataBlok: DataBlokRumahResponseItem) {
            binding.apply {
                val namaBlok = dataBlok.nama

                if (namaBlok == "all") {
                    "Seluruh Data Rumah".also { tvItemBlok.text = it }
                    binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.blue_100))
                } else {
                    "Blok $namaBlok".also { tvItemBlok.text = it }
                    binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.blue_50))
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, MainActivity::class.java)
                    intent.putExtra("namaBlok", namaBlok)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}