package uz.zero.payment

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PaymentMapper {

    fun toEntity(
        request: PaymentCreateRequest,
        productInfoProvider: (Long) -> Pair<String, BigDecimal>
    ): Payment {

        val payment = Payment(userId = request.userId)

        val items = request.items.map { itemRequest ->
            val (name, price) = productInfoProvider(itemRequest.productId)

            ProductItem(
                productId = itemRequest.productId,
                productName = name,
                quantity = itemRequest.quantity,
                unitPrice = price,
                totalPrice = price.multiply(BigDecimal(itemRequest.quantity)),
                payment = payment
            )
        }

        payment.items.addAll(items)
        payment.totalAmount = items.sumOf { it.totalPrice }

        return payment
    }

    fun toResponse(entity: Payment): PaymentResponse =
        PaymentResponse(
            id = entity.id,
            userId = entity.userId,
            totalAmount = entity.totalAmount,
            items = entity.items.map(::toItemResponse),
            createdDate = entity.createdDate,
            modifiedDate = entity.modifiedDate
        )

    private fun toItemResponse(item: ProductItem): ProductItemResponse =
        ProductItemResponse(
            id = item.id,
            productId = item.productId,
            productName = item.productName,
            quantity = item.quantity,
            unitPrice = item.unitPrice,
            totalPrice = item.totalPrice
        )
}
