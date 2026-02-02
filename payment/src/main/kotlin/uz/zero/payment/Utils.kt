package uz.zero.payment

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

fun currentUserId(): Long {
    val authentication = SecurityContextHolder.getContext().authentication
    if (authentication is JwtAuthenticationToken) {
        val details = authentication.details as? UserInfoResponse
        return details?.id ?: throw RuntimeException("User ID topilmadi")
    }
    throw RuntimeException("Avtorizatsiya xatosi")
}