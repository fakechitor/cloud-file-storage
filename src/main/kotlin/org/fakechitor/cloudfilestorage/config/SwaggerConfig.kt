package org.fakechitor.cloudfilestorage.config

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun logoutCustomizer() =
        OpenApiCustomizer { openApi ->
            openApi.paths.addPathItem(
                "/api/auth/logout",
                PathItem().post(
                    Operation().apply {
                        tags = listOf("Authorization")
                        summary = "Logout"
                        description = "Logout user and delete cookies"
                        responses =
                            ApiResponses().apply {
                                addApiResponse(
                                    "204",
                                    ApiResponse()
                                        .content(
                                            Content().addMediaType("application/json", MediaType()),
                                        ).description("Successful Logout"),
                                )
                            }
                    },
                ),
            )
        }
}
