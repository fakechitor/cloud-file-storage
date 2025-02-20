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
    summary = "Register user",
    description = "Register, authenticate user and retrieve information",
    responses = [
        ApiResponse(
            responseCode = "201",
            description = "Successfully registered user",
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
            responseCode = "409",
            description = "User already exists",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            {
                                    "message": "User already exists"
                            }
                        """,
                        ),
                    ],
                ),
            ],
        ),
    ],
)
annotation class RegisterUserDocs
