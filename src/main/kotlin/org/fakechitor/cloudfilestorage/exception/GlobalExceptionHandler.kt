package org.fakechitor.cloudfilestorage.exception

import org.fakechitor.cloudfilestorage.dto.response.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpServerErrorException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto(message = e.message))

    @ExceptionHandler(HttpServerErrorException::class)
    fun handleHttpServerErrorException(e: HttpServerErrorException): ResponseEntity<ErrorResponseDto> =
        ResponseEntity.status(e.statusCode).body(ErrorResponseDto(message = e.message))
}
