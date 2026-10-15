package com.smartx.rfidreader.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.color.MaterialColors
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.databinding.ItemHomeEpcBinding

class HomeEpcAdapter : ListAdapter<RfidTag, HomeEpcAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemHomeEpcBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: RfidTag) {
            val errorColor = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorError)
            val errorContainer = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorErrorContainer)
            val surfaceVariant = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorSurfaceVariant)
            val onSurface = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorOnSurface)
            val onSurfaceVariant = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorOnSurfaceVariant)
            val outlineVariant = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorOutlineVariant)

            val description = tag.description.trim().ifBlank {
                binding.root.context.getString(R.string.tag_description_loading)
            }

            binding.textDescription.text = description
            binding.textEpc.text = if (tag.objectIdCode.isNotBlank()) {
                binding.root.context.getString(R.string.tag_epc_with_idcode, tag.objectIdCode, tag.epc)
            } else {
                binding.root.context.getString(R.string.tag_epc_only, tag.epc)
            }

            if (tag.descriptionLookupError) {
                binding.root.setCardBackgroundColor(errorContainer)
                binding.root.strokeColor = errorColor
                binding.textDescription.setTextColor(errorColor)
                binding.textEpc.setTextColor(errorColor)
                binding.textEpc.alpha = 1f
            } else {
                binding.root.setCardBackgroundColor(surfaceVariant)
                binding.root.strokeColor = outlineVariant
                binding.textDescription.setTextColor(onSurface)
                binding.textEpc.setTextColor(onSurfaceVariant)
                binding.textEpc.alpha = 0.75f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeEpcBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<RfidTag>() {
        override fun areItemsTheSame(oldItem: RfidTag, newItem: RfidTag): Boolean = oldItem.epc == newItem.epc
        override fun areContentsTheSame(oldItem: RfidTag, newItem: RfidTag): Boolean =
            oldItem.description == newItem.description &&
                oldItem.objectIdCode == newItem.objectIdCode &&
                oldItem.descriptionLookupError == newItem.descriptionLookupError &&
                oldItem.readCount == newItem.readCount &&
                oldItem.rssi == newItem.rssi
    }
}
