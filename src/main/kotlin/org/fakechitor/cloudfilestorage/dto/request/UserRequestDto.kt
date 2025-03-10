package org.fakechitor.cloudfilestorage.dto.request

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRequestDto(
    @field:Size(min = 5, max = 20, message = "Login length must be between 5 and 20 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+[a-zA-Z_0-9]*[a-zA-Z0-9]+$", message = "Login dont math requirements")
    val username: String,
    @field:Size(min = 5, max = 20, message = "Password length must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>\\[\\]/`~+=-_';]*$", message = "Password dont match requirements")
    val password: String,
)
