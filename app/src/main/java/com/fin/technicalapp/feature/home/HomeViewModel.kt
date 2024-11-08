package com.fin.technicalapp.feature.home

import androidx.lifecycle.ViewModel
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.core.data.product.api.model.ProductCategories
import com.fin.technicalapp.core.data.product.api.model.ProductItem
import com.fin.technicalapp.core.data.product.api.repository.ProductRepository
import com.fin.technicalapp.core.utils.extensions.getLaunch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll

class HomeViewModel(
    private val productRepository: ProductRepository,
): ViewModel() {
    private val _product = MutableSharedFlow<AppResponse<ProductItem>>()
    val product = _product.asSharedFlow()

    private val _products = MutableSharedFlow<AppResponse<List<ProductItem>>>()
    val products = _products.asSharedFlow()

    private val _productsCategories = MutableSharedFlow<AppResponse<ProductCategories>>()
    val productsCategories = _productsCategories.asSharedFlow()

    private val _cartItems = MutableSharedFlow<AppResponse<List<CartItem>>>()
    val cartItems = _cartItems.asSharedFlow()

    private val _cartStatus = MutableSharedFlow<AppResponse<Boolean>>()
    val cartStatus = _cartStatus.asSharedFlow()

    fun getProduct(id: Int) {
        getLaunch {
            _product.emitAll(productRepository.getProduct(id))
        }
    }

    fun getProducts(categories: String = "") {
        getLaunch {
            _products.emitAll(productRepository.getProducts())
        }
    }

    fun getProductsCategories() {
        getLaunch {
            _productsCategories.emitAll(productRepository.getProductsCategories())
        }
    }

    fun getCartItems() {
        getLaunch {
            _cartItems.emitAll(productRepository.getCartItems())
        }
    }

    fun addCartItem(cartItem: CartItem) {
        getLaunch {
            _cartStatus.emitAll(productRepository.addToCart(cartItem))
        }
    }

    fun updateQuantityCartItem(productId: Int, quantity: Int) {
        getLaunch {
            _cartStatus.emitAll(productRepository.updateCartItemQuantity(productId, quantity))
        }
    }

    fun deleteFromCart(cartItem: CartItem) {
        getLaunch {
            _cartStatus.emitAll(productRepository.removeCartItem(cartItem))
        }
    }

    suspend fun clearAll(): Int = productRepository.clearCart()

}