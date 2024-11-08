package com.fin.technicalapp.feature.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.fin.technicalapp.feature.home.helper.ItemFilter

object FilterDiffCallback: DiffUtil.ItemCallback<ItemFilter>() {
    override fun areItemsTheSame(oldItem: ItemFilter, newItem: ItemFilter): Boolean {
        return oldItem.label == newItem.label
    }

    override fun areContentsTheSame(oldItem: ItemFilter, newItem: ItemFilter): Boolean {
        return oldItem == newItem
    }
}