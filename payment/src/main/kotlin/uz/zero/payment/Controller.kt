package uz.zero.payment

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("payment")
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: PaymentCreateRequest): PaymentResponse =
        paymentService.executePayment(request)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): PaymentResponse =
        paymentService.getById(id)

    @GetMapping
    fun getAll(): List<PaymentResponse> =
        paymentService.getAll()

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): List<PaymentResponse> =
        paymentService.getByUserId(userId)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) =
        paymentService.delete(id)

}
