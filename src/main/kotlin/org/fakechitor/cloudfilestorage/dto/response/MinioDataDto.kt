package org.fakechitor.cloudfilestorage.dto.response

import org.fakechitor.cloudfilestorage.util.MinioDataType

sealed class MinioDataDto(
    open val path: String,
    open val name: String,
    open val type: MinioDataType,
)
