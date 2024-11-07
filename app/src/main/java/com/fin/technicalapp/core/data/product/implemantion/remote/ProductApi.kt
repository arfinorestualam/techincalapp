package com.fin.technicalapp.core.data.product.implemantion.remote

import com.fin.technicalapp.core.data.product.implemantion.response.ProductResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): List<ProductResponseItem>

    @GET("products/categories")
    suspend fun getCategories(): List<String>?

    @GET("products/category/{categories}")
    suspend fun getProductsByCategory(
        @Path("categories") categories: String,
        ): List<ProductResponseItem>?

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
        ): ProductResponseItem?
}