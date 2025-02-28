package org.fakechitor.cloudfilestorage.dto.request

import jakarta.validation.constraints.Size

data class PathRequestDto(
    @field:Size(min = 3, max = 63, message = "Path length must between 3 and 63")
    val path: String?,
)
