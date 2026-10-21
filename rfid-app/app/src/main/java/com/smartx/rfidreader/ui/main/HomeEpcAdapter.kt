package com.smartx.shopeerecebimento.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.shopeerecebimento.R
import com.smartx.shopeerecebimento.databinding.ItemHomeEpcBinding

data class HomeDescriptionGroup(
    val description: String,
    val totalTags: Int
)

class HomeEpcAdapter : ListAdapter<HomeDescriptionGroup, HomeEpcAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemHomeEpcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: HomeDescriptionGroup) {
            binding.textDescription.text = binding.root.context.getString(
                R.string.home_group_description_count,
                group.description,
                group.totalTags,
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeEpcBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeDescriptionGroup>() {
        override fun areItemsTheSame(oldItem: HomeDescriptionGroup, newItem: HomeDescriptionGroup): Boolean =
            oldItem.description == newItem.description

        override fun areContentsTheSame(oldItem: HomeDescriptionGroup, newItem: HomeDescriptionGroup): Boolean =
            oldItem == newItem
    }
}
