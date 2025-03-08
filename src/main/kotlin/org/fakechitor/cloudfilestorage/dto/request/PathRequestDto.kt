package org.fakechitor.cloudfilestorage.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class PathRequestDto(
    @field:Size(max = 63, message = "Path length must between 3 and 63")
    @field:NotNull(message = "Path name must not be empty")
    val path: String?,
)
