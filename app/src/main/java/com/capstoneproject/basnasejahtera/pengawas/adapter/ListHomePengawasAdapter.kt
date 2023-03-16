package com.capstoneproject.basnasejahtera.pengawas.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.R
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.model.ItemData
import com.capstoneproject.basnasejahtera.pengawas.activity.MainPengawasActivity

class ListHomePengawasAdapter(private val listData: ArrayList<ItemData>) :
    RecyclerView.Adapter<ListHomePengawasAdapter.ListViewHolder>() {

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

                binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context,
                    R.color.blue_100))

                itemView.setOnClickListener {
                    val intent =
                        Intent(itemView.context, MainPengawasActivity::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
