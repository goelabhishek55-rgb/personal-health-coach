package com.personalcoach.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsResponse(
    @SerializedName("products") val products: List<ProductDto>? = null,
    @SerializedName("product") val product: ProductDto? = null
)

data class ProductDto(
    @SerializedName("product_name") val productName: String? = null,
    @SerializedName("brands") val brands: String? = null,
    @SerializedName("nutriments") val nutriments: NutrimentsDto? = null,
    @SerializedName("code") val barcode: String? = null
)

data class NutrimentsDto(
    @SerializedName("energy-kcal_100g") val energyKcal100g: Float? = null,
    @SerializedName("proteins_100g") val proteins100g: Float? = null,
    @SerializedName("carbohydrates_100g") val carbohydrates100g: Float? = null,
    @SerializedName("fat_100g") val fat100g: Float? = null,
    @SerializedName("fiber_100g") val fiber100g: Float? = null
)
