package org.fakechitor.cloudfilestorage.controller

import jakarta.validation.Valid
import org.fakechitor.cloudfilestorage.dto.request.PathRequestDto
import org.fakechitor.cloudfilestorage.service.MinioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/resource")
class ResourceController(
    private val minioService: MinioService,
) {
    @GetMapping("/{path}")
    fun getResourceInfo(
        @Valid @ModelAttribute pathDto: PathRequestDto,
    ) = ResponseEntity.ok().body(minioService.getFiles(pathDto.path))
}
