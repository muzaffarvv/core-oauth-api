package uz.zero.product

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

object JwtUtils {

    fun jwt(): Jwt =
        SecurityContextHolder.getContext().authentication.principal as Jwt

    fun username(): String =
        jwt().getClaimAsString("sub")

    fun roles(): List<String> =
        jwt().getClaimAsStringList("roles") ?: emptyList()
}
