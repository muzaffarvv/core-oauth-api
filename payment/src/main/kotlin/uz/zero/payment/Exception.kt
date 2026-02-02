package uz.zero.payment

enum class ErrorCodes(val code: Int) {

    INSUFFICIENT_FUNDS(300),
    INSUFFICIENT_STOCK(301),

    PAYMENT_NOT_FOUND(302),

}

sealed class BaseException(
    errorCode: ErrorCodes,
    override val message: String? = null
) : RuntimeException(message ?: errorCode.code.toString())

class InsufficientFundsException(message: String? = null) : BaseException(ErrorCodes.INSUFFICIENT_FUNDS, message)
class InsufficientStockException(message: String? = null) : BaseException(ErrorCodes.INSUFFICIENT_STOCK, message)
class PaymentNotFoundException(message: String? = null) : BaseException(ErrorCodes.PAYMENT_NOT_FOUND, message)


