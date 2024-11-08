package com.fin.technicalapp.feature.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fin.technicalapp.R
import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.databinding.ItemCartBinding

class CartAdapter(
    private val increaseQuantity: (CartItem) -> Unit,
    private val decreaseQuantity: (CartItem) -> Unit,
    private val removeItem: (CartItem) -> Unit,
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            Glide.with(binding.ivProductImage.context)
                .load(cartItem.imageUrl)
                .into(binding.ivProductImage)

            binding.tvProductTitle.text = cartItem.title
            binding.tvProductPrice.text = binding.root.context.getString(R.string.dollar_price, cartItem.price.toString())
            binding.tvQuantity.text = cartItem.quantity.toString()
            binding.btnIncrease.setOnClickListener {
                increaseQuantity(cartItem)
                currentPosition = position
            }
            binding.btnDecrease.setOnClickListener {
                decreaseQuantity(cartItem)
                currentPosition = position
            }
            binding.btnDelete.setOnClickListener {
                removeItem(cartItem)
                currentPosition = position
            }

        }
    }

    class CartDiffCallback: DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

    }

}