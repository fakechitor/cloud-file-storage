package org.fakechitor.cloudfilestorage.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.fakechitor.cloudfilestorage.docs.auth.AuthenticateUserDocs
import org.fakechitor.cloudfilestorage.docs.auth.RegisterUserDocs
import org.fakechitor.cloudfilestorage.dto.request.UserRequestDto
import org.fakechitor.cloudfilestorage.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @AuthenticateUserDocs
    @PostMapping("/sign-in")
    fun authenticateUser(
        @Valid @RequestBody userRequestDto: UserRequestDto,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) = ResponseEntity.ok(authService.authenticate(userRequestDto, request, response))

    @RegisterUserDocs
    @PostMapping("/sign-up")
    fun registerUser(
        @Valid @RequestBody userRequestDto: UserRequestDto,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) = ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRequestDto, request, response))
}
