package org.springframework.security.dsl.config.builders.server

import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange

/**
 * Configures [ServerHttpSecurity] using a [ServerHttpSecurity Kotlin DSL][ServerHttpSecurityDsl].
 *
 * Example:
 *
 * ```
 * @EnableWebFluxSecurity
 * class SecurityConfig {
 *
 *  @Bean
 *  fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
 *      return http {
 *          authorizeExchange {
 *              exchange("/public", permitAll)
 *              exchange(anyExchange, authenticated)
 *          }
 *       }
 *   }
 * }
 * ```
 *
 * @author Eleftheria Stein
 * @param httpConfiguration the configurations to apply to [ServerHttpSecurity]
 */
operator fun ServerHttpSecurity.invoke(httpConfiguration: ServerHttpSecurityDsl.() -> Unit): SecurityWebFilterChain =
        ServerHttpSecurityDsl(this, httpConfiguration).build()

/**
 * A [ServerHttpSecurity] Kotlin DSL created by [`http { }`][invoke]
 * in order to configure [ServerHttpSecurity] using idiomatic Kotlin code.
 *
 * @author Eleftheria Stein
 * @param init the configurations to apply to the provided [ServerHttpSecurity]
 */
class ServerHttpSecurityDsl(private val http: ServerHttpSecurity, private val init: ServerHttpSecurityDsl.() -> Unit) {

    /**
     * Allows restricting access based upon the [ServerWebExchange]
     *
     * Example:
     *
     * ```
     * @EnableWebFluxSecurity
     * class SecurityConfig {
     *
     *  @Bean
     *  fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
     *      return http {
     *          authorizeExchange {
     *              exchange("/public", permitAll)
     *              exchange(anyExchange, authenticated)
     *          }
     *       }
     *   }
     * }
     * ```
     *
     * @param authorizeExchangeConfiguration custom configuration that specifies
     * access for an exchange
     * @see [AuthorizeExchangeDsl]
     */
    fun authorizeExchange(authorizeExchangeConfiguration: AuthorizeExchangeDsl.() -> Unit) {
        val authorizeExchangeCustomizer = AuthorizeExchangeDsl().apply(authorizeExchangeConfiguration).get()
        this.http.authorizeExchange(authorizeExchangeCustomizer)
    }

    /**
     * Enables HTTP basic authentication.
     *
     * Example:
     *
     * ```
     * @EnableWebFluxSecurity
     * class SecurityConfig {
     *
     *  @Bean
     *  fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
     *      return http {
     *          httpBasic {
     *          }
     *       }
     *   }
     * }
     * ```
     *
     * @param httpBasicConfiguration custom configuration to be applied to the
     * HTTP basic authentication
     * @see [ServerHttpBasicDsl]
     */
    fun httpBasic(httpBasicConfiguration: ServerHttpBasicDsl.() -> Unit) {
        val httpBasicCustomizer = ServerHttpBasicDsl().apply(httpBasicConfiguration).get()
        this.http.httpBasic(httpBasicCustomizer)
    }

    /**
     * Apply all configurations to the provided [ServerHttpSecurity]
     */
    internal fun build(): SecurityWebFilterChain {
        init()
        return this.http.build()
    }
}