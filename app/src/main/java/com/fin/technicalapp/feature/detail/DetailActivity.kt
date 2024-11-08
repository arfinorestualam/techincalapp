package com.fin.technicalapp.feature.detail

import android.content.Context
import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.fin.technicalapp.R
import com.fin.technicalapp.core.component.BaseActivity
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.data.product.api.model.ProductItem
import com.fin.technicalapp.core.data.product.implemantion.mapper.toCartItem
import com.fin.technicalapp.core.utils.extensions.collectFlow
import com.fin.technicalapp.core.utils.extensions.showToast
import com.fin.technicalapp.databinding.ActivityDetailBinding
import com.fin.technicalapp.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: HomeViewModel by viewModel()
    companion object {
        private const val EXTRA_PRODUCT_ID = "product_id"

        fun start(context: Context, productId: Int) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_ID, productId)
            context.startActivity(intent)
        }
    }

    override fun setLayout(): View = binding.root


    override fun initView() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)
        if (productId != -1) {
            viewModel.getProduct(productId)
            setFlowCollector()
        } else {
            showToast("Product ID not found.")
            finish()
        }
    }

    private fun setFlowCollector() {
        collectFlow(viewModel.product) { appResponse ->
            when (appResponse) {
                is AppResponse.Error -> showToast(appResponse.message)
                is AppResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is AppResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    displayProductDetails(appResponse.data)
                }
                else -> Unit
            }
        }

        collectFlow(viewModel.cartStatus) { appResponse ->
            when (appResponse) {
                is AppResponse.Error -> showToast(appResponse.message)
                is AppResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is AppResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Succes add prodct to cart")
                }
                else -> Unit
            }
        }
    }

    private fun displayProductDetails(product: ProductItem) {
        with(binding) {
            tvTitle.text = product.title
            tvCategory.text = product.category
            tvDescription.text = product.description
            tvPrice.text = getString(R.string.dollar_price, product.price.toString())
            tvRating.text = getString(R.string.rating, product.rating.rate.toString())
            tvRatingCount.text = getString(R.string.reviews, product.rating.count.toString())

            Glide.with(ivProductImage.context)
                .load(product.image)
                .placeholder(R.drawable.ic_downloading)
                .into(ivProductImage)

            btnAddToCart.setOnClickListener {
                viewModel.addCartItem(product.toCartItem())
            }

            icBack.setOnClickListener {
                finish()
            }
        }
    }
}