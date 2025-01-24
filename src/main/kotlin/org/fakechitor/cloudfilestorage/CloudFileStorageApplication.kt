package org.fakechitor.cloudfilestorage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CloudFileStorageApplication

fun main(args: Array<String>) {
    runApplication<CloudFileStorageApplication>(*args)
}
