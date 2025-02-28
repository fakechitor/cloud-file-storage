package org.fakechitor.cloudfilestorage.controller

import jakarta.validation.Valid
import org.fakechitor.cloudfilestorage.dto.request.PathRequestDto
import org.fakechitor.cloudfilestorage.service.ResourceService
import org.springframework.http.HttpStatus
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
}
