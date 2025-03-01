package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.fakechitor.cloudfilestorage.exception.FileAlreadyExistsException
import org.fakechitor.cloudfilestorage.repository.ResourceRepository
import org.fakechitor.cloudfilestorage.util.MinioUtil
import org.fakechitor.cloudfilestorage.util.getObjectName
import org.fakechitor.cloudfilestorage.util.getObjectPath
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class ResourceService(
    private val resourceRepository: ResourceRepository,
    private val minioUtil: MinioUtil,
) {
    companion object {
        const val HOME_BUCKET = "user-files"
        const val UNKNOWN_FILE_NAME = "unknown"
    }

    fun getResourceInfo(path: String): MinioDataDto {
        val objectStats = resourceRepository.getObjectStats(path)
        return FileResponseDto(
            path = objectStats.`object`().getObjectPath(false),
            name = objectStats.`object`().getObjectName(false),
            size = objectStats.size(),
        )
    }

    fun deleteResource(path: String) = resourceRepository.deleteObject(path)

    fun downloadResource(path: String): Resource {
        val objectsList = resourceRepository.getListOfObjects(path)
        val resources =
            objectsList.map {
                resourceRepository.getObject(it.get().objectName())
            }
        val names: Queue<String> = LinkedList(objectsList.map { it.get().objectName() })
        return makeZipFile(resources, names)
    }

    private fun makeZipFile(
        resources: List<Resource>,
        names: Queue<String>,
    ): InputStreamResource {
        val byteArrayOutputStream = ByteArrayOutputStream()

        ZipOutputStream(byteArrayOutputStream).use { zipOut ->
            for (resource in resources) {
                val inputStream = resource.inputStream
                val zipEntry = ZipEntry(names.poll() ?: UNKNOWN_FILE_NAME)
                zipOut.putNextEntry(zipEntry)
                inputStream.use { it.copyTo(zipOut) }
                zipOut.closeEntry()
            }
        }

        val zipBytes = byteArrayOutputStream.toByteArray()
        return InputStreamResource(ByteArrayInputStream(zipBytes))
    }

    fun moveResource(
        pathFrom: String,
        pathTo: String,
    ): FileResponseDto {
        throwIfFileAlreadyExists(pathTo)
        resourceRepository.copyObject(pathFrom, pathTo)
        resourceRepository.deleteObject(pathFrom)
        val file = resourceRepository.getObjectStats(pathTo)
        return FileResponseDto(
            path = file.`object`().getObjectPath(false),
            name = file.`object`().getObjectName(false),
            size = file.size(),
        )
    }

    private fun throwIfFileAlreadyExists(pathTo: String) {
        runCatching { resourceRepository.getObjectStats(pathTo) }
            .onSuccess { throw FileAlreadyExistsException("File already exists in folder") }
    }

    fun findResourceByName(query: String): List<MinioDataDto> =
        resourceRepository.getListOfObjects("").filter { it.get().objectName().contains(query, ignoreCase = true) }.map {
            minioUtil.handle(it.get())
        }
}
