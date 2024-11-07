package com.fin.technicalapp.core.data.product.api.repository

import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.core.data.product.api.model.ProductCategories
import com.fin.technicalapp.core.data.product.api.model.ProductItem
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProductsCategories(): Flow<AppResponse<ProductCategories>>

    suspend fun getProducts(categories: String = ""): Flow<AppResponse<List<ProductItem>>>

    suspend fun getProduct(id: Int): Flow<AppResponse<ProductItem>>

    suspend fun addToCart(cartItem: CartItem): Flow<AppResponse<Boolean>>

    suspend fun getCartItems(): Flow<AppResponse<List<CartItem>>>

    suspend fun removeCartItem(cartItem: CartItem): Flow<AppResponse<Boolean>>

    suspend fun clearCart(): Int

    suspend fun updateCartItemQuantity(productId: Int, quantity: Int): Flow<AppResponse<Boolean>>
}