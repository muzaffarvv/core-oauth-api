package uz.zero.payment

import feign.RequestInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
class ResourceServerConfig(
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun resourceServerFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/error", "/actuator/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { resourceServer ->
                resourceServer.jwt {
                    it.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
        return http.build()
    }

    private fun jwtAuthenticationConverter(): Converter<Jwt, JwtAuthenticationToken> {
        return Converter { source ->
            val userDetailsJson = getHeader(USER_DETAILS_HEADER_KEY)?.decompress()
            val userDetails = userDetailsJson?.run { objectMapper.readValue(this, UserInfoResponse::class.java) }

            val username = userDetails?.username ?: (source.claims[USERNAME_KEY] as? String ?: "unknown")
            val authorities = mutableListOf<SimpleGrantedAuthority>()

            userDetails?.let {
                authorities.add(SimpleGrantedAuthority("ROLE_${it.role}"))
            }

            JwtAuthenticationToken(source, authorities, username).apply {
                details = userDetails
            }
        }
    }
}

class FeignOAuth2TokenConfig {
    @Bean
    fun feignOAuth2TokenInterceptor() = RequestInterceptor { requestTemplate ->
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication is JwtAuthenticationToken) {
            val accessToken = authentication.token.tokenValue
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        }

        getHeader(USER_DETAILS_HEADER_KEY)?.let {
            requestTemplate.header(USER_DETAILS_HEADER_KEY, it)
        }
    }
}