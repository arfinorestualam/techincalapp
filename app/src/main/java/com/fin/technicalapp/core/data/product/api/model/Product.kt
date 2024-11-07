package com.fin.technicalapp.core.data.product.api.model

data class ProductItem(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val category: String,
    val rating: Rating,
)

data class Rating(
    val rate: Double,
    val count: Int,
)
