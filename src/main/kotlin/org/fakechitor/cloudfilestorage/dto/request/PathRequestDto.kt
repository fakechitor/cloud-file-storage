package org.fakechitor.cloudfilestorage.dto.request

import jakarta.validation.constraints.Size

data class PathRequestDto(
    @field:Size(max = 200, message = "Path length must be smaller than 200")
    val path: String?,
)
