package org.fakechitor.cloudfilestorage.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fakechitor.cloudfilestorage.dto.UserRequestDto
import org.fakechitor.cloudfilestorage.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @Operation(summary = "Authenticate user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Authentication successful"),
            ApiResponse(responseCode = "400", description = "Entered credentials are invalid"),
        ],
    )
    @PostMapping("/login")
    fun authenticateUser(
        @RequestBody userRequestDto: UserRequestDto,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) = ResponseEntity.ok(authService.authenticate(userRequestDto, request, response))

    @Operation(summary = "Register user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User created"),
            ApiResponse(responseCode = "409", description = "User already exists"),
        ],
    )
    @PostMapping("/register")
    fun registerUser(
        @RequestBody userRequestDto: UserRequestDto,
    ) = ResponseEntity.ok(authService.register(userRequestDto))
}
