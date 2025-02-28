package org.fakechitor.cloudfilestorage.service

import io.minio.*
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class MinioService(
    private val minioClient: MinioClient,
) {
    companion object {
        const val HOME_BUCKET = "user-files"
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
