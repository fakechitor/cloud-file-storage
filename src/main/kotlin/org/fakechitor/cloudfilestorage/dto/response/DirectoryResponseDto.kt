package org.fakechitor.cloudfilestorage.dto.response

import org.fakechitor.cloudfilestorage.util.MinioDataType

data class DirectoryResponseDto(
    override val path: String,
    override val name: String,
    override val type: MinioDataType = MinioDataType.DIRECTORY,
) : MinioDataDto(path, name, type)
