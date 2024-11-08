package com.fin.technicalapp.feature.summary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fin.technicalapp.R
import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.databinding.ItemOrderSummaryBinding

class SummaryAdapter : ListAdapter<CartItem, SummaryAdapter.SummaryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding = ItemOrderSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SummaryViewHolder(private val binding: ItemOrderSummaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            with(binding) {
                Glide.with(ivSummaryProductImage.context)
                    .load(cartItem.imageUrl)
                    .into(ivSummaryProductImage)
                tvSummaryProductTitle.text = cartItem.title
                tvSummaryProductPrice.text =
                    binding.root.context.getString(R.string.dollar_price, cartItem.price.toString())
                tvSummaryProductQuantity.text =
                    binding.root.context.getString(R.string.quantity, cartItem.quantity.toString())
                tvSummaryTotalPriceProduct.text =
                    binding.root.context.getString(R.string.dollar_price, (cartItem.price * cartItem.quantity).toString())
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem == newItem
    }
}
