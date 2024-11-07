package com.fin.technicalapp.core.data.product.implemantion.mapper

import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.core.data.product.implemantion.database.entity.CartItemEntity

fun CartItemEntity.toCartItem() = CartItem(
    id = this.id.toInt(),
    productId = this.productId.toInt(),
    quantity = this.quantity,
    price = this.price,
    title = this.title,
    imageUrl = this.imageUrl,
)

fun CartItem.toCartItemEntity() = CartItemEntity(
    id = this.id.toLong(),
    productId = this.productId.toLong(),
    quantity = this.quantity,
    price = this.price,
    title = this.title,
    imageUrl = this.imageUrl,
)