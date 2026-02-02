package uz.zero.product

import java.math.BigDecimal

// Product -> ProductResponse
fun Product.toResponse() = ProductResponse(
    id = this.id ?: 0L,
    name = this.name,
    code = this.code,
    price = this.price ?: BigDecimal.ZERO,
    description = this.description,
    stockQuantity = this.stockQuantity
)

// ProductCreate -> Product
fun ProductCreate.toEntity(code: String) = Product(
    name = this.name,
    code = code,
    description = this.description,
    stockQuantity = this.stockQuantity
)

// ProductUpdate -> Product (update existing entity)
fun ProductUpdate.applyToEntity(product: Product): Product {
    this.name?.let { product.name = it }
    this.price?.let { product.price = it }
    this.description?.let { product.description = it }
    this.stockQuantity?.let { product.stockQuantity = it }
    return product
}


