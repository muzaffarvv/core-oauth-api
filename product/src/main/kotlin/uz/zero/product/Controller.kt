package uz.zero.product

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("product")
@Validated
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun create(@Valid @RequestBody dto: ProductCreate): ProductResponse = productService.create(dto)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ProductResponse = productService.getById(id)

    @GetMapping("/code/{code}")
    fun getByCode(@PathVariable code: String): ProductResponse = productService.getByCode(code)

    @GetMapping
    fun getAllP(): List<ProductResponse> = productService.getAll()

    @GetMapping("/not-deleted")
    fun getAllPNotDeleted(): List<ProductResponse> = productService.getAllNotDeleted()

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ProductUpdate
    ): ProductResponse = productService.update(id, dto)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Boolean = productService.delete(id)

    @PostMapping("/{id}/decrease")
    fun decreaseStock(
        @PathVariable id: Long,
        @RequestParam quantity: Int
    ): ProductResponse = productService.decreaseStock(id, quantity)

    @PostMapping("/{id}/increase")
    fun increaseStock(
        @PathVariable id: Long,
        @RequestParam quantity: Int
    ): ProductResponse = productService.increaseStock(id, quantity)

}
