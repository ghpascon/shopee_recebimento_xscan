package com.smartx.rfidreader.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.databinding.ItemUnregisteredEpcBinding

class HomeUnregisteredAdapter :
    ListAdapter<String, HomeUnregisteredAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemUnregisteredEpcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(epc: String) {
            binding.textUnregisteredEpc.text = epc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUnregisteredEpcBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}
