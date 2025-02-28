package org.fakechitor.cloudfilestorage.service

import io.minio.*
import io.minio.messages.Bucket
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MinioService(
    private val minioClient: MinioClient,
) {
    companion object {
        const val SLASH = "/"
        const val HOME_BUCKET = "user-files"
        const val EMPTY_STRING = ""
    }

    fun getAllBuckets(): MutableList<Bucket> = minioClient.listBuckets()

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
}
