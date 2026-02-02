package uz.zero.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.util.Date

data class PaymentCreateRequest(
    val userId: Long,
    val items: List<ProductItemRequest>
)

data class PaymentResponse(
    val id: Long?,
    val userId: Long,
    val totalAmount: BigDecimal,
    val items: List<ProductItemResponse>,
    val createdDate: Date?,
    val modifiedDate: Date?
)

data class ProductItemRequest(
    val productId: Long,
    val quantity: Int
)

data class ProductItemResponse(
    val id: Long?,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal
)

/**  --- Feign ---  **/

data class ProductResponse(
    val id: Long,
    val name: String,
    val code: String,
    val price: BigDecimal,
    val description: String?,
    val stockQuantity: Int
)

data class UserResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val balance: BigDecimal
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val balance: BigDecimal,
    val role: String,
)
