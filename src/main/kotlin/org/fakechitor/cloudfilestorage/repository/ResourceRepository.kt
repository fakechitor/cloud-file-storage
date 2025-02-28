package org.fakechitor.cloudfilestorage.repository

import io.minio.MinioClient
import io.minio.StatObjectArgs
import io.minio.StatObjectResponse
import io.minio.errors.ErrorResponseException
import org.fakechitor.cloudfilestorage.exception.PathNotExistsException
import org.fakechitor.cloudfilestorage.service.MinioService.Companion.HOME_BUCKET
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpServerErrorException

@Repository
class ResourceRepository(
    private val minioClient: MinioClient,
) {
    fun getObjectStats(path: String): StatObjectResponse =
        try {
            minioClient.statObject(
                StatObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(path)
                    .build(),
            )
        } catch (e: ErrorResponseException) {
            throw PathNotExistsException("Bucket with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
}
