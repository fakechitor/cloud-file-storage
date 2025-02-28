package org.fakechitor.cloudfilestorage.repository

import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.exception.PathNotExistsException
import org.fakechitor.cloudfilestorage.service.ResourceService.Companion.HOME_BUCKET
import org.springframework.core.io.InputStreamResource
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
            throw PathNotExistsException("Resource with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    fun deleteObject(path: String) =
        try {
            minioClient.removeObject(
                RemoveObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(path)
                    .build(),
            )
        } catch (e: ErrorResponseException) {
            throw PathNotExistsException("Resource with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    fun getListOfObjects(path: String): Iterable<Result<Item>> =
        minioClient.listObjects(
            ListObjectsArgs
                .builder()
                .bucket(HOME_BUCKET)
                .prefix(path)
                .recursive(true)
                .build(),
        )

    fun getObject(objectName: String): InputStreamResource =
        try {
            InputStreamResource(
                minioClient.getObject(
                    GetObjectArgs
                        .builder()
                        .bucket(HOME_BUCKET)
                        .`object`(objectName)
                        .build(),
                ),
            )
        } catch (e: ErrorResponseException) {
            throw PathNotExistsException("Resource with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    fun copyObject(
        pathFrom: String?,
        pathTo: String?,
    ): ObjectWriteResponse =
        minioClient.copyObject(
            CopyObjectArgs
                .builder()
                .bucket(HOME_BUCKET)
                .`object`(pathTo)
                .source(
                    CopySource
                        .builder()
                        .bucket(HOME_BUCKET)
                        .`object`(pathFrom)
                        .build(),
                ).build(),
        )
}
