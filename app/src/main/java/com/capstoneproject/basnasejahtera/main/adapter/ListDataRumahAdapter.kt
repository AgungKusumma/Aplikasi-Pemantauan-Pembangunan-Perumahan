package com.capstoneproject.basnasejahtera.main.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.main.detail.DetailActivity
import com.capstoneproject.basnasejahtera.model.DataRumahResponseItem
import java.util.*

class ListDataRumahAdapter : RecyclerView.Adapter<ListDataRumahAdapter.ListViewHolder>() {

    private var listDataRumah = ArrayList<DataRumahResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListRumah(menu: List<DataRumahResponseItem>) {
        listDataRumah.clear()
        listDataRumah.addAll(menu)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listDataRumah[position])
    }

    override fun getItemCount(): Int = listDataRumah.size

    class ListViewHolder(private val binding: ItemRowDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataRumah: DataRumahResponseItem) {
            binding.apply {
                tvItemBlok.text = dataRumah.nomorRumah

                when (dataRumah.statusRumah) {
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
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("idRumah", dataRumah.id)
                    intent.putExtra("nomorRumah", dataRumah.nomorRumah)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}