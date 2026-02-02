package uz.zero.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

open class BaseService<T : BaseEntity>(
    protected val repository: BaseRepository<T>
) {

    protected open fun getEntity(id: Long): Optional<T> = repository.findById(id)

    fun findAll(): List<T> = repository.findAll()

    @Transactional
    open fun save(entity: T): T = repository.save(entity)

    @Transactional
    open fun softDelete(entity: T) {
        entity.deleted = true
        repository.save(entity)
    }

    fun findAllNotDeleted(): List<T> = repository.findAllByDeletedFalse()
}

@Service
class ProductService(
    repository: ProductRepository
) : BaseService<Product>(repository) {

    @Transactional
    fun create(dto: ProductCreate): ProductResponse {
        if (repository.existsByNameIgnoreCaseAndDeletedFalse(dto.name)) {
            throw ProductAlreadyExistsException("Product with name '${dto.name}' already exists")
        }

        val code = generateUniqueProductCode()
        val product = dto.toEntity(code)
        return save(product).toResponse()
    }

    private fun getProduct(id: Long): Product =
        super.getEntity(id).orElseThrow { ProductNotFoundException("Product with id $id not found") }

    fun getById(id: Long): ProductResponse =
        getProduct(id).toResponse()

    fun getByCode(code: String): ProductResponse =
        repository.findByCodeAndDeletedFalse(code)
            ?.toResponse()
            ?: throw ProductNotFoundException("Product with code '$code' not found")

    fun getAllNotDeleted(): List<ProductResponse> =
        super.findAllNotDeleted().map { it.toResponse() }

    fun getAll(): List<ProductResponse> = super.findAll().map { it.toResponse() }

    @Transactional
    fun update(id: Long, dto: ProductUpdate): ProductResponse {
        val product = getProduct(id)
        dto.applyToEntity(product)
        return save(product).toResponse()
    }

    @Transactional
    fun delete(id: Long): Boolean {
        val product = getEntity(id).orElse(null) ?: return false
        softDelete(product)
        return true
    }

    @Transactional
    fun decreaseStock(productId: Long, quantity: Int): ProductResponse {
        val product = getProduct(productId)
        if (product.stockQuantity < quantity) {
            throw InsufficientStockException("Product ${product.name} has only ${product.stockQuantity} in stock")
        }
        product.stockQuantity -= quantity
        return save(product).toResponse()
    }

    @Transactional
    fun increaseStock(productId: Long, quantity: Int): ProductResponse {
        val product = getProduct(productId)
        product.stockQuantity += quantity
        return save(product).toResponse()
    }

    private fun generateUniqueProductCode(): String {
        var code: String
        do {
            code = generateProductCode()
        } while (repository.existsByCode(code))
        return code
    }

    private fun generateProductCode(): String {
        val now = LocalDate.now()
        val yearShort = now.year % 100
        val month = now.monthValue
        val randomPart = Random.nextInt(100, 1000)

        // P-2654202 (Yil, Random, Oy)
        return "P-%02d%03d%02d".format(yearShort, randomPart, month)
    }
}