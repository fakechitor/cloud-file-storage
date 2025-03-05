package org.fakechitor.cloudfilestorage.repository

import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.exception.DirectoryNotExistsException
import org.fakechitor.cloudfilestorage.service.ResourceService.Companion.HOME_BUCKET
import org.fakechitor.cloudfilestorage.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpServerErrorException
import java.io.ByteArrayInputStream

@Repository
class DirectoryRepository(
    private val minioClient: MinioClient,
    private val userService: UserService,
) {
    fun getObjectsInDirectory(path: String): MutableIterable<Result<Item>> =
        try {
            minioClient.listObjects(
                ListObjectsArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .prefix(getParent() + path)
                    .build(),
            )
        } catch (e: ErrorResponseException) {
            throw DirectoryNotExistsException("Directory with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    fun putObject(
        path: String,
        byteArray: ByteArray,
    ): ObjectWriteResponse {
        try {
            return minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(getParent() + path)
                    .stream(ByteArrayInputStream(byteArray), byteArray.size.toLong(), -1)
                    .contentType("application/x-directory")
                    .build(),
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun getParent() = userService.getParentFolderNameForUser()
}
