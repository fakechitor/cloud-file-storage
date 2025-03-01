package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.exception.DirectoryNotExistsException
import org.fakechitor.cloudfilestorage.repository.DirectoryRepository
import org.fakechitor.cloudfilestorage.util.MinioUtil
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
}
