package com.fin.technicalapp.feature.summary

import android.view.View
import com.fin.technicalapp.R
import com.fin.technicalapp.core.component.BaseActivity
import com.fin.technicalapp.core.data.AppResponse
import com.fin.technicalapp.core.utils.extensions.collectFlow
import com.fin.technicalapp.core.utils.extensions.showToast
import com.fin.technicalapp.databinding.ActivitySummaryBinding
import com.fin.technicalapp.feature.home.HomeViewModel
import com.fin.technicalapp.feature.summary.adapter.SummaryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SummaryActivity: BaseActivity() {
    private lateinit var binding: ActivitySummaryBinding

    private val viewModel: HomeViewModel by viewModel()

    override fun setLayout(): View = binding.root

    private val summaryAdapter by lazy {
        SummaryAdapter()
    }

    override fun initView() {
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        viewModel.getCartItems()
        with(binding) {
            rvOrderSummary.adapter = summaryAdapter
            ivBack.setOnClickListener {
                finish()
            }
        }

        setFlowCollector()
    }

    private fun setFlowCollector() {
        collectFlow(viewModel.cartItems) { appResponse ->
            when(appResponse) {
                is AppResponse.Error -> {
                    showToast(appResponse.message)
                }

                is AppResponse.Loading -> {

                }

                is AppResponse.Success -> {
                    summaryAdapter.submitList(appResponse.data)
                    val totalPrice = appResponse.data.sumOf { it.price * it.quantity }
                    binding.tvTotalPrice.text = getString(R.string.total_dollar_price, String.format("%.2f", totalPrice))
                }

                else -> Unit
            }
        }
    }
}
