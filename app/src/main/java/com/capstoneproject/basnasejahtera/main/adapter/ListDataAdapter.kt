package com.capstoneproject.basnasejahtera.main.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.basnasejahtera.databinding.ItemRowDataBinding
import com.capstoneproject.basnasejahtera.main.DetailActivity
import com.capstoneproject.basnasejahtera.model.ItemData

class ListDataAdapter(private val listMenu: ArrayList<ItemData>) :
    RecyclerView.Adapter<ListDataAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMenu[position])
    }

    override fun getItemCount(): Int = listMenu.size

    inner class ListViewHolder(private val binding: ItemRowDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rumah: ItemData) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(rumah.photo)
                    .into(ivHouse)
                tvItemBlok.text = rumah.id

                when (rumah.info) {
                    "SOLD" -> {
                        binding.root.setBackgroundColor(Color.GREEN)
                    }
                    "BOOKED" -> {
                        binding.root.setBackgroundColor(Color.YELLOW)
                    }
                    "SELL" -> {
                        binding.root.setBackgroundColor(Color.WHITE)
                    }
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("Rumah", rumah)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
