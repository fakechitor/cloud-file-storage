package org.fakechitor.cloudfilestorage.dto.response

import org.fakechitor.cloudfilestorage.util.MinioDataType

data class FileResponseDto(
    override val path: String,
    override val name: String,
    val size: Long,
    override val type: MinioDataType = MinioDataType.FILE,
) : MinioDataDto(path, name, type)
