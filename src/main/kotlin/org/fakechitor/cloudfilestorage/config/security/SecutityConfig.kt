package org.fakechitor.cloudfilestorage.config.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fakechitor.cloudfilestorage.service.UserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            authenticationManager
            securityContext {
                securityContextRepository = securityContextRepository()
            }
            securityMatcher("/api/**")
            authorizeHttpRequests {
                authorize(HttpMethod.POST, "/api/auth/**", permitAll)
                authorize(HttpMethod.GET, "/api/admin/**", hasRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.IF_REQUIRED
            }

            logout {
                logoutUrl = "/api/auth/sign-out"
                invalidateHttpSession = true
                clearAuthentication = true
                deleteCookies("JSESSIONID")
                logoutSuccessHandler = logoutSuccessHandler()
            }
            exceptionHandling {
                authenticationEntryPoint = authenticationEntryPoint()
                accessDeniedHandler = accessDeniedHandler()
            }
        }

        return http.build()
    }

    @Bean
    fun authenticationProvider() =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder())
        }

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager = configuration.authenticationManager

    @Bean
    fun securityContextRepository(): SecurityContextRepository = HttpSessionSecurityContextRepository()

    @Bean
    fun securityContextHolder(): SecurityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint =
        AuthenticationEntryPoint { request, response, exception ->
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler =
        AccessDeniedHandler { request, response, exception ->
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
        }

    @Bean
    fun logoutSuccessHandler(): LogoutSuccessHandler =
        LogoutSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse, authentication: Authentication? ->
            response.status = 204
            response.contentType = "application/json"
        }
}
