package org.fakechitor.cloudfilestorage.service

import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.springframework.stereotype.Service

@Service
class MinioService(
    private val userService: UserService,
) {
    companion object {
        const val HOME_BUCKET = "user-files"
        const val UNKNOWN_FILE_NAME = "unknown"
        const val MAX_FILE_SIZE = 20971520
    }

    fun handleObjects(
        item: Item,
        path: String,
    ): MinioDataDto =
        when (item.isDir) {
            true -> handleDirectory(item, path)

            false -> handleFile(item, path)
        }

    fun handleObjects(item: Item): MinioDataDto =
        when (item.isDir) {
            true -> {
                DirectoryResponseDto(
                    path = item.objectName().removePrefix(userService.getParentFolderNameForUser()).getObjectPath(true) + "/",
                    name = item.objectName().getObjectName(true) + "/",
                )
            }
            false -> {
                FileResponseDto(
                    path = item.objectName().removePrefix(userService.getParentFolderNameForUser()).getObjectPath(false) + "/",
                    name = item.objectName().getObjectName(false),
                    size = item.size(),
                )
            }
        }

    fun handleDirectory(
        item: Item,
        path: String,
    ): DirectoryResponseDto {
        val pathResponse =
            when (path.isEmpty()) {
                true -> ""
                false -> path.getObjectPath(true) + "/"
            }
        return DirectoryResponseDto(
            path = pathResponse,
            name = item.objectName().getObjectName(true) + "/",
        )
    }

    fun handleFile(
        item: Item,
        path: String,
    ): FileResponseDto {
        val pathResponse =
            when (path.isEmpty()) {
                true -> ""
                false -> path.getObjectPath(false) + "/"
            }
        return FileResponseDto(
            path = pathResponse,
            name = item.objectName().getObjectName(false),
            size = item.size(),
        )
    }

    fun getParentPath(): String = userService.getParentFolderNameForUser()
}

fun String.getObjectName(isDirectory: Boolean): String =
    when (isDirectory) {
        true -> this.removeSuffix("/").substringAfterLast("/")
        false -> this.substringAfterLast("/")
    }

fun String.getObjectPath(isDirectory: Boolean): String =
    when (isDirectory) {
        true -> this.removeSuffix("/").substringBeforeLast("/")
        false -> this.substringBeforeLast("/")
    }
