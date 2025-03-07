package org.fakechitor.cloudfilestorage.repository

import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.exception.DirectoryNotExistsException
import org.fakechitor.cloudfilestorage.exception.PathNotExistsException
import org.fakechitor.cloudfilestorage.service.MinioService.Companion.HOME_BUCKET
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream

@Repository
class MinioRepository(
    private val minioClient: MinioClient,
) {
    fun getStatObject(path: String): StatObjectResponse =
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

    fun getListObjects(
        path: String,
        isRecursive: Boolean,
    ): MutableIterable<Result<Item>> =
        try {
            minioClient.listObjects(
                ListObjectsArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .prefix(path)
                    .recursive(isRecursive)
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
        file: MultipartFile,
    ): ObjectWriteResponse =
        try {
            minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(path)
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

    fun putObject(
        path: String,
        byteArray: ByteArray,
    ): ObjectWriteResponse {
        try {
            return minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(HOME_BUCKET)
                    .`object`(path)
                    .stream(ByteArrayInputStream(byteArray), byteArray.size.toLong(), -1)
                    .contentType("application/x-directory")
                    .build(),
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun removeObject(path: String) =
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
        pathFrom: String,
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
