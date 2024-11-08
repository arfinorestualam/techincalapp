package com.fin.technicalapp.feature.cart

import android.content.Intent
import android.view.View
import com.fin.technicalapp.R
import com.fin.technicalapp.core.component.BaseActivity
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.utils.extensions.collectFlow
import com.fin.technicalapp.core.utils.extensions.showToast
import com.fin.technicalapp.databinding.ActivityCartBinding
import com.fin.technicalapp.feature.cart.adapter.CartAdapter
import com.fin.technicalapp.feature.home.HomeViewModel
import com.fin.technicalapp.feature.summary.SummaryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private val viewModel: HomeViewModel by viewModel()

    private val adapter by lazy {
        CartAdapter(
            increaseQuantity = {
                viewModel.updateQuantityCartItem(it.productId, it.quantity)
            },
            decreaseQuantity = {
                if (it.quantity > 1) {
                    viewModel.updateQuantityCartItem(it.productId, it.quantity)
                } else showToast("Minimum quantity is 1")
            },
            removeItem = {
                viewModel.deleteFromCart(it)
            }
        )
    }

    override fun setLayout(): View = binding.root

    override fun initView() {
        binding = ActivityCartBinding.inflate(layoutInflater)
        viewModel.getCartItems()
        with(binding) {
            rvCartItems.adapter = adapter
            btnCheckout.setOnClickListener {
                val intent = Intent(this@CartActivity, SummaryActivity::class.java)
                startActivity(intent)
            }
            ivBack.setOnClickListener {
                finish()
            }
        }

        setFlow()
    }

    private fun setFlow() {
        collectFlow(viewModel.cartItems) { appResponse ->
            when(appResponse) {
                is AppResponse.Error -> {
                    showToast(appResponse.message)
                }
                is AppResponse.Loading -> {

                }
                is AppResponse.Empty -> {
                    showToast("Cart is empty")
                    binding.btnCheckout.isEnabled = false
                    adapter.submitList(null)
                    binding.tvValueTotalPrice.text = getString(R.string.zero)
                }
                is AppResponse.Success -> {
                    adapter.submitList(appResponse.data)
                    val totalPrice = appResponse.data.sumOf { it.price * it.quantity }
                    binding.tvValueTotalPrice.text = getString(R.string.dollar_price, String.format("%.2f", totalPrice))
                    binding.btnCheckout.isEnabled = true
                }
            }
        }

        collectFlow(viewModel.cartStatus) { appResponse ->
            when (appResponse) {
                is AppResponse.Error -> {
                    showToast(appResponse.message)
                }
                is AppResponse.Loading -> {

                }
                is AppResponse.Success -> {
                    viewModel.getCartItems()
                }
                else -> Unit
            }
        }
    }
}