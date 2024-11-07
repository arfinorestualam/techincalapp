package com.fin.technicalapp.core.data.product.implemantion.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val quantity: Int = 1,
    val price: Double,
    val imageUrl: String? = null,
    val title: String,
)
