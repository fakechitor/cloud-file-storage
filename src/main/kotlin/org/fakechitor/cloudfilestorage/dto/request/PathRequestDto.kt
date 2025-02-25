package org.fakechitor.cloudfilestorage.dto.request

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class PathRequestDto(
    @field:Size(min = 3, max = 63, message = "Path length must between 3 and 63")
    @field:Pattern(regexp = "[a-z0-9.-]+", message = "Path must contain only english letters, numbers and -.")
    val path: String,
)
