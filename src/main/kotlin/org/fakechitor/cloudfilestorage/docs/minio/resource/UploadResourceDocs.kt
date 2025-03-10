package org.fakechitor.cloudfilestorage.docs.minio.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.fakechitor.cloudfilestorage.dto.response.ErrorResponseDto
import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    tags = ["Minio"],
    summary = "Upload resource",
    description = "Upload resource",
    responses = [
        ApiResponse(
            responseCode = "201",
            description = "Resources successfully uploaded",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = FileResponseDto::class),
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
            responseCode = "404",
            description = "Resource with that path not found",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            {
                                "message": "Resource with that path does not exist"
                            }
                        """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "409",
            description = "Resource with that name already exists",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponseDto::class),
                    examples = [
                        ExampleObject(
                            value = """
                            {
                                "message": "Resource with that name already exists"
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
annotation class UploadResourceDocs
