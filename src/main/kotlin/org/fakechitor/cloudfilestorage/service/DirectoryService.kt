package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.exception.DirectoryNotExistsException
import org.fakechitor.cloudfilestorage.repository.DirectoryRepository
import org.fakechitor.cloudfilestorage.util.MinioUtil
import org.fakechitor.cloudfilestorage.util.getObjectName
import org.fakechitor.cloudfilestorage.util.getObjectPath
import org.springframework.stereotype.Service

@Service
class DirectoryService(
    private val directoryRepository: DirectoryRepository,
    private val minioUtil: MinioUtil,
) {
    fun getObjectsInDirectory(path: String) =
        directoryRepository
            .getObjectsInDirectory(path)
            .map { minioUtil.handle(it.get()) }
            .takeIf {
                it.isNotEmpty()
            } ?: throw DirectoryNotExistsException("Directory $path does not exist")

    fun createEmptyDirectory(path: String): DirectoryResponseDto {
        throwIfParentFolderNotExists(path)
        val directory = directoryRepository.putObject(path, ByteArray(0)).`object`()
        return DirectoryResponseDto(
            path = directory.getObjectPath(true),
            name = directory.getObjectName(true),
        )
    }

    private fun throwIfParentFolderNotExists(path: String) =
        runCatching {
            getObjectsInDirectory(path.removeSuffix("/").substringBeforeLast("/") + "/")
        }.onFailure { throw DirectoryNotExistsException("Parent directory does not exist") }
}
