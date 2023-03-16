package com.capstoneproject.basnasejahtera.pengawas.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.basnasejahtera.databinding.ItemRowPembangunanBinding
import com.capstoneproject.basnasejahtera.model.ItemDataProgress
import com.capstoneproject.basnasejahtera.pengawas.activity.UpdateStatusPembangunanActivity

class ListDataProgressAdapter(private val listData: ArrayList<ItemDataProgress>) :
    RecyclerView.Adapter<ListDataProgressAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowPembangunanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(private val binding: ItemRowPembangunanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ItemDataProgress) {
            val persentase = data.persentase
            val detailProgress = data.detailProgress

            binding.apply {
                "$persentase%".also { tvProgress.text = it }
                tvDetailProgress.text = detailProgress

                itemView.setOnClickListener {
                    val intent =
                        Intent(itemView.context, UpdateStatusPembangunanActivity::class.java)
                    intent.putExtra("persentaseProgress", persentase)
                    intent.putExtra("detailProgress", detailProgress)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
