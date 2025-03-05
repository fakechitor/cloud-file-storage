package org.fakechitor.cloudfilestorage.repository

import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.exception.PathNotExistsException
import org.fakechitor.cloudfilestorage.service.ResourceService.Companion.HOME_BUCKET
import org.fakechitor.cloudfilestorage.service.UserService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.multipart.MultipartFile

@Repository
class ResourceRepository(
    private val minioClient: MinioClient,
    private val userService: UserService,
) {
    fun getObjectStats(path: String): StatObjectResponse =
        try {
            minioClient.statObject(
                StatObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(getParent() + path)
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
                    .`object`(getParent() + path)
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
                .prefix(getParent() + path)
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
                        .`object`(getParent() + objectName)
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
                .`object`(getParent() + pathTo)
                .source(
                    CopySource
                        .builder()
                        .bucket(HOME_BUCKET)
                        .`object`(getParent() + pathFrom)
                        .build(),
                ).build(),
        )

    fun putResource(
        path: String,
        file: MultipartFile,
    ): ObjectWriteResponse =
        try {
            minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(getParent() + path)
                    .stream(
                        file.inputStream,
                        file.size,
                        -1,
                    ).contentType(file.contentType)
                    .build(),
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    private fun getParent() = userService.getParentFolderNameForUser()
}
