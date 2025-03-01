package org.fakechitor.cloudfilestorage.controller

import org.fakechitor.cloudfilestorage.dto.request.PathRequestDto
import org.fakechitor.cloudfilestorage.service.DirectoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/directory")
class DirectoryController(
    private val directoryService: DirectoryService,
) {
    @GetMapping
    fun directoryInfo(
        @ModelAttribute path: PathRequestDto,
    ) = ResponseEntity.ok().body(directoryService.getObjectsInDirectory(path.path ?: ""))
}
