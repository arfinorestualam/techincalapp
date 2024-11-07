package com.fin.technicalapp.core.data.product.implemantion.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fin.technicalapp.core.data.product.implemantion.database.entity.CartItemEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemById(productId: Int): List<CartItemEntity>

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantityCartItem(productId: Int, quantity: Int): Int

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity): Int

    @Query("DELETE FROM cart_items")
    suspend fun deleteAllCartItems(): Int
}