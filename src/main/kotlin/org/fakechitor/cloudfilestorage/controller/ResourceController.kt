package org.fakechitor.cloudfilestorage.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.fakechitor.cloudfilestorage.dto.request.PathRequestDto
import org.fakechitor.cloudfilestorage.service.ResourceService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/resource")
class ResourceController(
    private val resourceService: ResourceService,
) {
    @GetMapping
    fun getResourceInfo(
        @Valid @ModelAttribute path: PathRequestDto,
    ) = ResponseEntity.ok().body(resourceService.getResourceInfo(path.path ?: ""))

    @DeleteMapping
    fun deleteResource(
        @Valid @ModelAttribute path: PathRequestDto,
    ) = ResponseEntity.status(HttpStatus.NO_CONTENT).body(resourceService.deleteResource(path.path ?: ""))

    @GetMapping("/download")
    fun downloadResource(
        @Valid @ModelAttribute path: PathRequestDto,
        response: HttpServletResponse,
    ): ResponseEntity<Resource> =
        ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resourceService.downloadResource(path.path))
}
