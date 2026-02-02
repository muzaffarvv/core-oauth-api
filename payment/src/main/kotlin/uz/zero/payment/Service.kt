package uz.zero.payment

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

abstract class BaseService<T : BaseEntity, CreateDto, ResponseDto>() {

    @Transactional
    abstract fun executePayment(dto: CreateDto): ResponseDto

    @Transactional(readOnly = true)
    abstract fun getById(id: Long): ResponseDto

    @Transactional(readOnly = true)
    abstract fun getAll(): List<ResponseDto>

    @Transactional
    abstract fun delete(id: Long)
}

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper,
    private val userClient: UserClient,
    private val productClient: ProductClient
) : BaseService<Payment, PaymentCreateRequest, PaymentResponse>() {

    // ================= CREATE =================
    @Transactional
    override fun executePayment(dto: PaymentCreateRequest): PaymentResponse {

        val userId = currentUserId()

        val payment = buildPayment(dto)

        processUserPayment(userId, payment.totalAmount)

        decreaseProductStock(dto)

        val saved = paymentRepository.save(payment)

        return paymentMapper.toResponse(saved)
    }

    // ================= PRIVATE HELPERS =================

    private fun buildPayment(dto: PaymentCreateRequest): Payment {
        return paymentMapper.toEntity(dto) { productId ->
            val product = productClient.getById(productId)
            val quantity = dto.items.first { it.productId == productId }.quantity

            if (quantity > product.stockQuantity) {
                throw InsufficientStockException("Product ${product.name} has only ${product.stockQuantity} in stock")
            }

            product.name to product.price
        }
    }

    private fun processUserPayment(userId: Long, amount: BigDecimal) {
        val user = userClient.getById(userId)
        if (user.balance < amount) {
            throw InsufficientFundsException("Insufficient funds")
        }

        userClient.withdraw(userId, amount)
    }

    private fun decreaseProductStock(dto: PaymentCreateRequest) {
        dto.items.forEach { item ->
            productClient.decreaseStock(item.productId, item.quantity)
        }
    }

    private fun getPayment(id: Long): Payment = paymentRepository.findByIdAndDeletedFalse(id)
        ?: throw PaymentNotFoundException("Payment not found")

    // ================= READ =================

    @Transactional(readOnly = true)
    override fun getById(id: Long): PaymentResponse {
        val payment = getPayment(id)
        return paymentMapper.toResponse(payment)
    }

    @Transactional(readOnly = true)
    fun getByUserId(userId: Long): List<PaymentResponse> =
        paymentRepository.findAllByUserIdAndDeletedFalse(userId)
            .map(paymentMapper::toResponse)

    @Transactional(readOnly = true)
    override fun getAll(): List<PaymentResponse> =
        paymentRepository.findAllByDeletedFalse()
            .map(paymentMapper::toResponse)

    @Transactional
    override fun delete(id: Long) {
        val payment = getPayment(id)
        payment.deleted = true
        paymentRepository.save(payment)
    }
}
