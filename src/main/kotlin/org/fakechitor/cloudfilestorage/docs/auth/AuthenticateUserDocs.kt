package org.fakechitor.cloudfilestorage.docs.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.fakechitor.cloudfilestorage.dto.response.ErrorResponseDto
import org.fakechitor.cloudfilestorage.dto.response.UserResponseDto

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    tags = ["Authorization"],
    summary = "Authorize user",
    description = "Authorize user and retrieve user information",
    responses = [
        ApiResponse(
            responseCode = "200",
            description = "Successful user authorization",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                        {
                            "login": "shmurdyak"
                        }
                    """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            {
                                "message": "Failed to login with username: venom"
                            }
                        """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            {
                                "message": "Internal Server Error"
                            }
                        """,
                        ),
                    ],
                ),
            ],
        ),
    ],
)
annotation class AuthenticateUserDocs
