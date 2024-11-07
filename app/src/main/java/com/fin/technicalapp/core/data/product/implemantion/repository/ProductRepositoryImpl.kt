package com.fin.technicalapp.core.data.product.implemantion.repository

import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.core.data.product.api.model.ProductCategories
import com.fin.technicalapp.core.data.product.api.model.ProductItem
import com.fin.technicalapp.core.data.product.api.repository.ProductRepository
import com.fin.technicalapp.core.data.product.implemantion.database.dao.CartDao
import com.fin.technicalapp.core.data.product.implemantion.mapper.toCartItem
import com.fin.technicalapp.core.data.product.implemantion.mapper.toCartItemEntity
import com.fin.technicalapp.core.data.product.implemantion.mapper.toProductItem
import com.fin.technicalapp.core.data.product.implemantion.mapper.toProductsCategories
import com.fin.technicalapp.core.data.product.implemantion.remote.ProductApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val cartDao: CartDao,
) : ProductRepository {
    override suspend fun getProductsCategories(): Flow<AppResponse<ProductCategories>> =
        flow {
            emit(AppResponse.Loading)
            try {
                val response = productApi.getCategories()
                when {
                    response.isNullOrEmpty() -> {
                        emit(AppResponse.Error("No data found"))
                    }
                    else -> {
                        emit(AppResponse.Success(response.toProductsCategories()))
                    }
                }
            } catch (e: Exception) {
                emit(AppResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getProducts(categories: String): Flow<AppResponse<List<ProductItem>>> =
        flow {
            emit(AppResponse.Loading)
            try {
               val response =
                   if (categories.isEmpty()) productApi.getProducts()
                   else productApi.getProductsByCategory(categories)

                when {
                    response.isNullOrEmpty() -> {
                        emit(AppResponse.Error("No data found"))
                    }
                    else -> {
                        emit(AppResponse.Success(response.map { it.toProductItem() }))
                    }
                }
            } catch (e: Exception) {
                emit(AppResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getProduct(id: Int): Flow<AppResponse<ProductItem>> =
        flow {
            emit(AppResponse.Loading)
            try {
                val response = productApi.getProductById(id)
                when {
                    response!!.title.isNullOrEmpty() -> {
                        emit(AppResponse.Error("No data found"))
                    }
                    else -> {
                        emit(AppResponse.Success(response.toProductItem()))
                    }
                }

            } catch (e: Exception) {
                emit(AppResponse.Error(e.toString()))
            }


        }.flowOn(Dispatchers.IO)

    override suspend fun addToCart(cartItem: CartItem): Flow<AppResponse<Boolean>> {
        return flow {
            emit(AppResponse.Loading)
            val check = cartDao.getCartItemById(cartItem.productId)
            if (check.isNotEmpty()) {
                val product = check.first()
                cartDao.updateQuantityCartItem(
                    product.productId.toInt(), product.quantity + 1
                )
            } else {
                cartDao.insertCartItem(cartItem.toCartItemEntity())
            }
            emit(AppResponse.Success(true))
        }
            .catch { AppResponse.Error("Error adding to cart") }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getCartItems(): Flow<AppResponse<List<CartItem>>> {
        return flow {
            emit(AppResponse.Loading)
            val check = cartDao.getAllCartItems()
            when {
                check.isEmpty() -> {
                    emit(AppResponse.Error("No data found"))
                }
                else -> {
                    emit(AppResponse.Success(check.map { it.toCartItem() }))
                }
            }
        }
            .catch { AppResponse.Error("Error getting cart items") }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun removeCartItem(cartItem: CartItem): Flow<AppResponse<Boolean>> {
        return flow {
            emit(AppResponse.Loading)
            val check = cartDao.deleteCartItem(cartItem.toCartItemEntity())
            if(check > 0) {
                emit(AppResponse.Success(true))
            } else {
                emit(AppResponse.Error("Failed removing cart item"))
            }
        }
            .catch { AppResponse.Error("Error removing cart item") }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun clearCart(): Int = cartDao.deleteAllCartItems()

    override suspend fun updateCartItemQuantity(
        productId: Int,
        quantity: Int
    ): Flow<AppResponse<Boolean>> {
        return flow {
            emit(AppResponse.Loading)
            val check = cartDao.updateQuantityCartItem(productId, quantity)
            if(check > 0) {
                emit(AppResponse.Success(true))
            } else {
                emit(AppResponse.Error("Failed updating cart item quantity"))
            }
        }
            .catch { AppResponse.Error("Error updating cart item quantity") }
            .flowOn(Dispatchers.IO)
    }
}