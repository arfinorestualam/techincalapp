package com.fin.technicalapp.core.data.product.implemantion.mapper

import com.fin.technicalapp.core.data.product.api.model.CartItem
import com.fin.technicalapp.core.data.product.api.model.ProductItem
import com.fin.technicalapp.core.data.product.api.model.Rating
import com.fin.technicalapp.core.data.product.implemantion.response.ProductResponseItem
import com.fin.technicalapp.core.data.product.implemantion.response.RatingResponse

fun ProductResponseItem.toProductItem() = ProductItem(
    image = this.image ?: "",
    price = this.price ?: 0.0,
    title = this.title ?: "",
    id = this.id ?: 0,
    description = this.description ?: "",
    category = this.category ?: "",
    rating = this.rating.toRating(),
)

fun RatingResponse?.toRating() = Rating(
    count = this?.count ?: 0,
    rate = this?.rate ?: 0.0,
)

fun ProductItem.toCartItem() = CartItem(
    productId = this.id,
    title = this.title,
    price = this.price,
    imageUrl = this.image,
)