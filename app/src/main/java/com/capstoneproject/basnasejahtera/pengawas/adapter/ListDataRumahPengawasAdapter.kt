package com.capstoneproject.basnasejahtera.pengawas.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.model.DataRumahResponseItem
import com.capstoneproject.basnasejahtera.pengawas.activity.UpdateStatusPembangunanActivity

class ListDataRumahPengawasAdapter :
    RecyclerView.Adapter<ListDataRumahPengawasAdapter.ListViewHolder>() {
    private var listDataRumahPengawas = ArrayList<DataRumahResponseItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListRumahPengawas(data: List<DataRumahResponseItem>) {
        listDataRumahPengawas.clear()
        listDataRumahPengawas.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listDataRumahPengawas[position])
    }

    override fun getItemCount(): Int = listDataRumahPengawas.size

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
                    val intent =
                        Intent(itemView.context, UpdateStatusPembangunanActivity::class.java)
                    intent.putExtra("idRumah", dataRumah.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}