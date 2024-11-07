package com.fin.technicalapp.core.data.product.implemantion.response

import com.google.gson.annotations.SerializedName

data class ProductResponseItem(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("rating")
    val rating: RatingResponse? = null,

    @SerializedName("description")
    val description: String? = null,
)

data class RatingResponse(
    @SerializedName("rate")
    val rate: Double? = null,

    @SerializedName("count")
    val count: Int? = null,
)