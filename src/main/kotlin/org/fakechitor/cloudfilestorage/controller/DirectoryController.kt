package org.fakechitor.cloudfilestorage.controller

import jakarta.validation.Valid
import org.fakechitor.cloudfilestorage.docs.minio.directory.CreateEmptyDirectoryDocs
import org.fakechitor.cloudfilestorage.docs.minio.directory.GetDirectoryInfoDocs
import org.fakechitor.cloudfilestorage.dto.request.PathRequestDto
import org.fakechitor.cloudfilestorage.service.DirectoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/directory")
class DirectoryController(
    private val directoryService: DirectoryService,
) {
    @GetMapping
    @GetDirectoryInfoDocs
    fun directoryInfo(
        @Valid @ModelAttribute path: PathRequestDto,
    ) = ResponseEntity.ok().body(directoryService.getObjectsInDirectory(path.path ?: ""))

    @PostMapping
    @CreateEmptyDirectoryDocs
    fun createDirectory(
        @Valid @ModelAttribute path: PathRequestDto,
    ) = ResponseEntity.status(HttpStatus.CREATED).body(directoryService.createEmptyDirectory(path.path ?: ""))
}
