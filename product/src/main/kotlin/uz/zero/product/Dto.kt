package uz.zero.product

import java.math.BigDecimal
import jakarta.validation.constraints.*

// Create
data class ProductCreate(
    @field:NotBlank(message = "Product name cannot be blank")
    @field:Size(max = 72, message = "Product name cannot exceed 72 characters")
    val name: String,

    @field:NotNull(message = "Price is required")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    val price: BigDecimal,

    @field:Size(max = 120, message = "Description cannot exceed 120 characters")
    val description: String? = null,

    @field:Min(value = 0, message = "Stock quantity cannot be negative")
    val stockQuantity: Int = 0
)

// Update
data class ProductUpdate(
    @field:Size(max = 72, message = "Product name cannot exceed 72 characters")
    val name: String? = null,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    val price: BigDecimal? = null,

    @field:Size(max = 120, message = "Description cannot exceed 120 characters")
    val description: String? = null,

    @field:Min(value = 0, message = "Stock quantity cannot be negative")
    val stockQuantity: Int? = null
)

// Response
data class ProductResponse(
    val id: Long,
    val name: String,
    val code: String,
    val price: BigDecimal,
    val description: String?,
    var stockQuantity: Int
)
