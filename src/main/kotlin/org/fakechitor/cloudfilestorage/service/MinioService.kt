package org.fakechitor.cloudfilestorage.service

import io.minio.*
import io.minio.errors.ErrorResponseException
import io.minio.messages.Bucket
import io.minio.messages.Item
import org.fakechitor.cloudfilestorage.dto.response.DirectoryResponseDto
import org.fakechitor.cloudfilestorage.dto.response.FileResponseDto
import org.fakechitor.cloudfilestorage.dto.response.MinioDataDto
import org.fakechitor.cloudfilestorage.exception.PathNotExistsException
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    private val minioClient: MinioClient,
) {
    private val slash = "/"

    fun getAllBuckets(): MutableList<Bucket> = minioClient.listBuckets()

    fun createBucket(bucketName: String) =
        minioClient.makeBucket(
            MakeBucketArgs
                .builder()
                .bucket(bucketName)
                .build(),
        )

    fun uploadBucket(
        bucketName: String,
        file: MultipartFile,
    ) {
        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucketName)
                .`object`(file.name)
                .stream(
                    file.inputStream,
                    file.size,
                    -1,
                ).contentType(file.contentType)
                .build(),
        )
    }

    fun downloadFile(
        bucketName: String,
        fileName: String,
    ): Resource {
        val inputStream =
            minioClient.getObject(
                GetObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .build(),
            )

        return InputStreamResource(inputStream)
    }

    fun getFiles(filePath: String): List<MinioDataDto> {
        try {
            return handleMinioObjects(
                filePath = filePath,
                objectsList =
                    minioClient
                        .listObjects(
                            ListObjectsArgs.builder().bucket(filePath).build(),
                        ),
            )
        } catch (e: ErrorResponseException) {
            throw PathNotExistsException("Bucket with that path does not exist")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun handleMinioObjects(
        filePath: String,
        objectsList: Iterable<Result<Item>>,
    ) = objectsList.map { item ->
        when (item.get().isDir) {
            true -> {
                DirectoryResponseDto(
                    path = filePath + slash + item.get().objectName(),
                    name = item.get().objectName(),
                )
            }
            false -> {
                FileResponseDto(
                    path = filePath + slash + item.get().objectName(),
                    name = item.get().objectName(),
                    size = item.get().size(),
                )
            }
        }
    }
}
