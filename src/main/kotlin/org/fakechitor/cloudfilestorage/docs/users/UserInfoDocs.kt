package org.fakechitor.cloudfilestorage.docs.users

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
    tags = ["UserInfo"],
    summary = "Retrieve user info",
    description = "Retrieve user information",
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
                            "username": "artemveranda2k17"
                        }
                    """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            {
                                "message": "User is unauthorized"
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
annotation class UserInfoDocs
