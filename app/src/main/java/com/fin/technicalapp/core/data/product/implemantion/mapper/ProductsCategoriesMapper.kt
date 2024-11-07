package com.fin.technicalapp.core.data.product.implemantion.mapper

import com.fin.technicalapp.core.data.product.api.model.ProductCategories

fun List<String>?.toProductsCategories() = ProductCategories(
    categories = this ?: listOf()
)