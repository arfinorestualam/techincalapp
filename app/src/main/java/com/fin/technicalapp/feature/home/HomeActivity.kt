package com.fin.technicalapp.feature.home

import android.content.Intent
import android.view.View
import com.fin.technicalapp.core.component.BaseActivity
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.utils.extensions.collectFlow
import com.fin.technicalapp.core.utils.extensions.getLaunch
import com.fin.technicalapp.core.utils.extensions.gone
import com.fin.technicalapp.core.utils.extensions.showToast
import com.fin.technicalapp.core.utils.extensions.visible
import com.fin.technicalapp.databinding.ActivityHomeBinding
import com.fin.technicalapp.feature.cart.CartActivity
import com.fin.technicalapp.feature.detail.DetailActivity
import com.fin.technicalapp.feature.home.adapter.FilterAdapter
import com.fin.technicalapp.feature.home.adapter.ProductAdapter
import com.fin.technicalapp.feature.home.helper.ItemFilter
import com.fin.technicalapp.feature.login.LoginActivity
import com.fin.technicalapp.feature.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity: BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val authViewModel: LoginViewModel by viewModel()

    private val viewModel: HomeViewModel by viewModel()

    private val filterAdapter by lazy {
        FilterAdapter(::onFilterClicked)
    }

    private val productAdapter by lazy {
        ProductAdapter(::onProductClicked)
    }

    override fun setLayout(): View = binding.root

    override fun initView() {
        binding = ActivityHomeBinding.inflate(layoutInflater)

        viewModel.getProductsCategories()
        viewModel.getProducts()
        with(binding) {
            rvCategories.adapter = filterAdapter
            rvCategories.itemAnimator = null

            rvProducts.adapter = productAdapter
            ivProfile.setOnClickListener {
                showProfile()
            }
            ivCart.setOnClickListener {
                startActivity(Intent(this@HomeActivity, CartActivity::class.java))
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                viewModel.getProducts(    filterAdapter.currentList.find { it.isActive }?.value ?: "")
            }
        }

        setFlowCollector()
    }

    private fun showProfile() {
        getLaunch {
            ProfileBottomSheet.newInstance(authViewModel.readUserData()).apply {
                onLogoutClick = {
                    getLaunch {
                        if (authViewModel.clearUserData() && viewModel.clearAll() != -1){
                            dismiss()
                            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }.show(supportFragmentManager, ProfileBottomSheet.TAG)
        }
    }

    private fun setFlowCollector() {
        collectFlow(viewModel.productsCategories) { appResponse ->
            when(appResponse) {
                is AppResponse.Error -> {
                    showToast(appResponse.message)
                }

                is AppResponse.Loading -> {

                }

                is AppResponse.Success -> {
                    val itemFilterList = arrayListOf<ItemFilter>()
                    itemFilterList.add(ItemFilter("all", "", true))
                    itemFilterList.addAll(
                        appResponse.data.categories.map { category ->
                            ItemFilter(label = category, value = category, isActive = false)
                        }
                    )

                    filterAdapter.submitList(itemFilterList)
                }

                else -> Unit
            }
        }

        collectFlow(viewModel.products) { appResponse ->
            when(appResponse) {
                is AppResponse.Error -> {
                    showToast(appResponse.message)
                }

                is AppResponse.Loading -> {
                    productAdapter.submitList(null)
                    binding.shimmerProduct.startShimmer()
                    binding.shimmerProduct.visible()
                }

                is AppResponse.Success -> {
                    productAdapter.submitList(appResponse.data)
                    binding.shimmerProduct.stopShimmer()
                    binding.shimmerProduct.gone()
                }

                else -> Unit
            }
        }
    }

    private fun onFilterClicked(f: ItemFilter) {
        filterAdapter.currentList.find {
            it.isActive
        }?.isActive = false
        f.isActive = true
        filterAdapter.notifyItemRangeChanged(0, filterAdapter.itemCount)
        viewModel.getProducts(f.value)
    }

    private fun onProductClicked(id: Int) {
        DetailActivity.start(this@HomeActivity, id)
    }

}