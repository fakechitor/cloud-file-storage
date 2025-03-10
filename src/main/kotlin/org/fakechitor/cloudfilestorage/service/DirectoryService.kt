package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.fakechitor.cloudfilestorage.exception.DirectoryNotExistsException
import org.fakechitor.cloudfilestorage.exception.FileAlreadyExistsException
import org.fakechitor.cloudfilestorage.repository.*
import org.springframework.stereotype.Service

@Service
class DirectoryService(
    private val minioRepository: MinioRepository,
    private val minioService: MinioService,
    private val userService: UserService,
) {
    fun getObjectsInDirectory(path: String): List<MinioDataDto> =
        minioRepository
            .getListObjects(path = minioService.getParentPath() + path, isRecursive = false)
            .run {
                map { minioService.handleObjects(it.get(), path) }
                    .takeIf { it.isNotEmpty() } ?: throw DirectoryNotExistsException("Directory $path does not exist")
            }.filter { it.name.isNotEmpty() }

    fun createEmptyDirectory(path: String): DirectoryResponseDto {
        throwIfFolderAlreadyExists(path)
        val fullPath = minioService.getParentPath() + path
        throwIfParentFolderNotExists(fullPath)
        val dir = minioRepository.putObject(path = fullPath, byteArray = ByteArray(0)).`object`()
        return DirectoryResponseDto(
            path = dir.getObjectPath(true),
            name = dir.getObjectName(true),
        )
    }

    private fun throwIfFolderAlreadyExists(path: String) {
        runCatching {
            minioRepository.getStatObject(minioService.getParentPath() + path)
        }.onSuccess {
            throw FileAlreadyExistsException("Folder with that name already exists")
        }
    }

    fun createDirectoryForNewUser() = minioRepository.putObject(path = userService.getParentFolderNameForUser(), byteArray = ByteArray(0))

    private fun throwIfParentFolderNotExists(path: String) =
        runCatching {
            minioRepository.getListObjects(path = minioService.getParentPath() + path, isRecursive = true)
        }.onFailure { throw DirectoryNotExistsException("Parent directory does not exist") }
}
