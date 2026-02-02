package uz.zero.product

enum class ErrorCodes(val code: Int, val msg: String) {

    PRODUCT_NOT_FOUND(300, "Product not found"),
    PRODUCT_ALREADY_EXISTS(301, "Product already exists"),
    INSUFFICIENT_STOCK(302, "Insufficient stock"),


}

sealed class BaseException(
    val errorCode: ErrorCodes,
    override val message: String? = null
) : RuntimeException(message ?: errorCode.msg)

class ProductNotFoundException(message: String? = null) : BaseException(ErrorCodes.PRODUCT_NOT_FOUND, message)
class ProductAlreadyExistsException(message: String? = null) : BaseException(ErrorCodes.PRODUCT_ALREADY_EXISTS, message)
class InsufficientStockException(message: String? = null) : BaseException(ErrorCodes.INSUFFICIENT_STOCK, message)