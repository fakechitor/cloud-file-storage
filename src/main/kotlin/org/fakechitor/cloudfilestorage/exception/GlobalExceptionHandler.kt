package org.fakechitor.cloudfilestorage.exception

import org.fakechitor.cloudfilestorage.dto.response.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpServerErrorException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto(message = e.message))

    @ExceptionHandler(HttpServerErrorException::class)
    fun handleHttpServerErrorException(e: HttpServerErrorException) =
        ResponseEntity.status(e.statusCode).body(ErrorResponseDto(message = e.message))

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(e: BadCredentialsException) =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponseDto(message = e.message))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to e.fieldErrors.map { it.defaultMessage }))

    @ExceptionHandler(PathNotExistsException::class)
    fun handlePathNotExistsException(e: PathNotExistsException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto(message = e.message))

    @ExceptionHandler(FileAlreadyExistsException::class)
    fun handleFileAlreadyExistsException(e: FileAlreadyExistsException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto(message = e.message))

    @ExceptionHandler(DirectoryNotExistsException::class)
    fun handleDirectoryNotExistsException(e: DirectoryNotExistsException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto(message = e.message))
}
