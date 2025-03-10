package org.fakechitor.cloudfilestorage.docs.minio.directory

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.dto.response.ErrorResponseDto

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    tags = ["Minio"],
    summary = "Retrieve directory information",
    description = "Retrieve information about all objects in directory",
    responses = [
        ApiResponse(
            responseCode = "200",
            description = "Directory information retrieved successfully",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = DirectoryResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            [
                                {
                                    "path": "folder1/folder2/", 
                                    "name": "file.txt",
                                    "size": 123, 
                                    "type": "FILE" 
                                }
                            ]
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
                                "message": [
                                    "Path length must be smaller than 200"
                                ]
                            }
                        """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
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
            responseCode = "404",
            description = "Directory not found",
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
annotation class GetDirectoryInfoDocs
