package org.fakechitor.cloudfilestorage.dto.request

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRequestDto(
    @field:Size(min = 4, max = 40, message = "Login length must be between 4 and 40 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login must contain only english letters and numbers")
    val login: String,
    @field:Size(min = 4, max = 40, message = "Password length must be between 4 and 40 characters")
    val password: String,
)
