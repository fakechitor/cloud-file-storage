package org.fakechitor.cloudfilestorage.repository

import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.Result
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.exception.PathNotExistsException
import org.fakechitor.cloudfilestorage.service.ResourceService.Companion.HOME_BUCKET
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpServerErrorException

@Repository
class DirectoryRepository(
    private val minioClient: MinioClient,
) {
    fun getObjectsInDirectory(path: String): MutableIterable<Result<Item>> =
        try {
            minioClient.listObjects(
                ListObjectsArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .prefix(path)
                    .build(),
            )
        } catch (e: ErrorResponseException) {
            throw PathNotExistsException("Directory with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
}
