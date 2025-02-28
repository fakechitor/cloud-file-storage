package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.fakechitor.cloudfilestorage.repository.ResourceRepository
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
) {
    companion object {
        const val HOME_BUCKET = "user-files"
        const val UNKNOWN_FILE_NAME = "unknown"
    }

    fun getResourceInfo(path: String): MinioDataDto {
        val objectStats = resourceRepository.getObjectStats(path)
        return FileResponseDto(
            path = objectStats.`object`().getObjectPath(),
            name = objectStats.`object`().getObjectName(),
            size = objectStats.size(),
        )
    }

    fun deleteResource(path: String) = resourceRepository.deleteObject(path)

    fun downloadResource(path: String?): Resource {
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
}

private fun String.getObjectName(): String = this.split("/").last()

private fun String.getObjectPath(): String =
    this
        .split("/")
        .dropLast(1)
        .joinToString("/")
