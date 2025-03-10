package org.fakechitor.cloudfilestorage.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?,
    ) {
        response?.apply {
            contentType = "application/json"
            status = HttpServletResponse.SC_UNAUTHORIZED
            writer.write(
                ObjectMapper().writeValueAsString(
                    mapOf(
                        "message" to "User is unauthorized",
                    ),
                ),
            )
        }
    }
}
