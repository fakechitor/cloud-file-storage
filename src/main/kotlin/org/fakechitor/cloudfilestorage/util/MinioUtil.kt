package org.fakechitor.cloudfilestorage.util

import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.springframework.stereotype.Component

@Component
class MinioUtil {
    fun handle(item: Item): MinioDataDto =
        when (item.isDir) {
            true -> {
                DirectoryResponseDto(
                    path = item.objectName().getObjectPath(true) + "/",
                    name = item.objectName().getObjectName(true) + "/",
                )
            }
            false -> {
                FileResponseDto(
                    path = item.objectName().getObjectPath(false) + "/",
                    name = item.objectName().getObjectName(false),
                    size = item.size(),
                )
            }
        }
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
