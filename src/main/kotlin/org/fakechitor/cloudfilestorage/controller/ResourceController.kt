package org.fakechitor.cloudfilestorage.controller

import jakarta.validation.Valid
import org.fakechitor.cloudfilestorage.dto.request.PathRequestDto
import org.fakechitor.cloudfilestorage.service.ResourceService
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
}
