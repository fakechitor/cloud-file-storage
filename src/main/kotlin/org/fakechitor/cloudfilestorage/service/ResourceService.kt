package org.fakechitor.cloudfilestorage.service

import io.minio.StatObjectResponse
import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.fakechitor.cloudfilestorage.repository.ResourceRepository
import org.springframework.stereotype.Service

@Service
class ResourceService(
    private val resourceRepository: ResourceRepository,
) {
    fun getResourceInfo(path: String): MinioDataDto {
        val objectStats = resourceRepository.getObjectStats(path)
        return FileResponseDto(
            path = objectStats.getObjectPath(),
            name = objectStats.getObjectName(),
            size = objectStats.size(),
        )
    }

    fun deleteResource(path: String) = resourceRepository.deleteObject(path)
}

private fun StatObjectResponse.getObjectName(): String = this.`object`().split("/").last()

private fun StatObjectResponse.getObjectPath(): String =
    this
        .`object`()
        .split("/")
        .dropLast(1)
        .joinToString("/")
