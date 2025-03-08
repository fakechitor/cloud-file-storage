package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.fakechitor.cloudfilestorage.exception.FileAlreadyExistsException
import org.fakechitor.cloudfilestorage.exception.FileSizeLimitExceededException
import org.fakechitor.cloudfilestorage.repository.MinioRepository
import org.fakechitor.cloudfilestorage.service.MinioService.Companion.MAX_FILE_SIZE
import org.fakechitor.cloudfilestorage.service.MinioService.Companion.UNKNOWN_FILE_NAME
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class ResourceService(
    private val minioService: MinioService,
    private val minioRepository: MinioRepository,
) {
    fun getResourceInfo(path: String): MinioDataDto {
        val objectStats = minioRepository.getStatObject(minioService.getParentPath() + path)
        return FileResponseDto(
            path = objectStats.`object`().getObjectPath(false) + "/",
            name = objectStats.`object`().getObjectName(false),
            size = objectStats.size(),
        )
    }

    fun deleteResource(path: String) = minioRepository.removeObject(minioService.getParentPath() + path)

    fun downloadResource(path: String): Resource {
        if (path.endsWith("/")) {
            val objectsList =
                minioRepository.getListObjects(
                    path = minioService.getParentPath() + getPathForDownload(path),
                    isRecursive = true,
                )
            val resources = objectsList.map { minioRepository.getObject(it.get().objectName()) }
            val names: Queue<String> = LinkedList(objectsList.map { getPathForZipFile(pathToFile = it.get().objectName()) })
            return makeZipFile(resources, names)
        } else {
            return minioRepository.getObject(minioService.getParentPath() + path)
        }
    }

    private fun getPathForDownload(path: String) = path.substringBeforeLast("/")

    private fun getPathForZipFile(pathToFile: String): String = pathToFile.removePrefix(minioService.getParentPath())

    private fun makeZipFile(
        resources: List<InputStreamResource>,
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
    ): MinioDataDto {
        throwIfFileAlreadyExists(pathTo)
        return when (pathFrom.endsWith("/")) {
            true -> moveDirectory(pathFrom = pathFrom, pathTo = pathTo)
            false -> moveFile(pathFrom = pathFrom, pathTo = pathTo)
        }
    }

    private fun moveDirectory(
        pathFrom: String,
        pathTo: String,
    ): DirectoryResponseDto {
        minioRepository.putObject(path = minioService.getParentPath() + pathTo, byteArray = ByteArray(0))
        minioRepository.getListObjects(minioService.getParentPath() + pathFrom, true).let {
            it.forEach {
                minioRepository.copyObject(
                    pathFrom = minioService.getParentPath() + pathFrom + getPathForObjectInFolder(it.get().objectName(), pathFrom),
                    pathTo = minioService.getParentPath() + pathTo + getPathForObjectInFolder(it.get().objectName(), pathFrom),
                )
            }
            it.forEach { minioRepository.removeObject(it.get().objectName()) }
        }
        val folder = minioRepository.getStatObject(minioService.getParentPath() + pathTo)
        return DirectoryResponseDto(
            path = folder.`object`().getObjectPath(true) + "/",
            name = folder.`object`().getObjectName(true) + "/",
        )
    }

    private fun getPathForObjectInFolder(
        path: String,
        pathFrom: String,
    ) = path.removePrefix(minioService.getParentPath() + pathFrom).substringAfter("/")

    private fun moveFile(
        pathFrom: String,
        pathTo: String,
    ): FileResponseDto {
        minioRepository.copyObject(
            pathFrom = minioService.getParentPath() + pathFrom,
            pathTo = minioService.getParentPath() + pathTo,
        )
        minioRepository.removeObject(minioService.getParentPath() + pathFrom)
        val file = minioRepository.getStatObject(minioService.getParentPath() + pathTo)
        return FileResponseDto(
            path = file.`object`().getObjectPath(false) + "/",
            name = file.`object`().getObjectName(false),
            size = file.size(),
        )
    }

    private fun throwIfFileAlreadyExists(pathTo: String) {
        runCatching { minioRepository.getStatObject(minioService.getParentPath() + pathTo) }
            .onSuccess { throw FileAlreadyExistsException("File already exists in folder") }
    }

    fun findResourceByName(query: String): List<MinioDataDto> =
        minioRepository
            .getListObjects(
                path = minioService.getParentPath(),
                isRecursive = true,
            ).filter { it.get().objectName().contains(query, ignoreCase = true) }
            .map { minioService.handleObjects(it.get()) }

    fun uploadResource(
        path: String,
        file: List<MultipartFile>,
    ): List<MinioDataDto> {
        throwIfFileSizeIsBig(file)
        val data: MutableList<MinioDataDto> = mutableListOf()
        file.forEach {
            minioRepository.putObject(minioService.getParentPath() + path + it.originalFilename, it).apply {
                data.add(
                    FileResponseDto(
                        path = path,
                        name = it.originalFilename ?: UNKNOWN_FILE_NAME,
                        size = it.size,
                    ),
                )
            }
        }
        return data
    }

    private fun throwIfFileSizeIsBig(file: List<MultipartFile>) {
        var sum = 0
        file.forEach { sum += it.size.toInt() }
        if (sum > MAX_FILE_SIZE) throw FileSizeLimitExceededException("Upload files size should be lower than 20 mb")
    }
}
