package uz.zero.payment

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@FeignClient(name = "auth-service", url = "\${services.hosts.auth}")
interface UserClient {

    @GetMapping("/user/{id}")
    fun getById(@PathVariable("id") id: Long): UserResponse

    @PostMapping("/user/{id}/withdraw")
    fun withdraw(
        @PathVariable("id") id: Long, @RequestParam("amount") amount: BigDecimal
    )

    @PostMapping("/user/{id}/deposit")
    fun deposit(
        @PathVariable("id") id: Long, @RequestParam("amount") amount: BigDecimal
    )
}

@FeignClient(name = "product-service", url = "\${services.hosts.product}")
interface ProductClient {

    @GetMapping("/product/{id}")
    fun getById(@PathVariable id: Long): ProductResponse

    @PostMapping("/product/{id}/decrease")
    fun decreaseStock(@PathVariable id: Long, @RequestParam quantity: Int)

    @PostMapping("/product/{id}/increase")
    fun increaseStock(@PathVariable id: Long, @RequestParam quantity: Int)
}
