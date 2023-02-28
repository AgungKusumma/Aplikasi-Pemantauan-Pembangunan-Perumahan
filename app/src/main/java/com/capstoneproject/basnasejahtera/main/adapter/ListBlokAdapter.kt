package com.capstoneproject.basnasejahtera.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                "Blok ${dataBlok.nama}".also { tvItemBlok.text = it }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, MainActivity::class.java)
                    intent.putExtra("namaBlok", dataBlok.nama)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}