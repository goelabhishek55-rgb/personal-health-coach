package com.personalcoach.domain.usecase

import com.personalcoach.data.remote.OpenFoodFactsApi
import com.personalcoach.data.remote.dto.ProductDto
import javax.inject.Inject

class SearchFoodUseCase @Inject constructor(
    private val api: OpenFoodFactsApi
) {
    suspend operator fun invoke(query: String): List<ProductDto> {
        return try {
            val response = api.searchFood(query)
            response.products ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getByBarcode(barcode: String): ProductDto? {
        return try {
            val response = api.getProductByBarcode(barcode)
            response.product
        } catch (e: Exception) {
            null
        }
    }
}
