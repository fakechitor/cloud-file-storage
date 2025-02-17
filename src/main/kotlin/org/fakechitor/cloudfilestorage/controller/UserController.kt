package org.fakechitor.cloudfilestorage.controller

import org.fakechitor.cloudfilestorage.dto.UserResponseDto
import org.fakechitor.cloudfilestorage.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/users/{id}")
    fun getUser(
        @PathVariable id: String,
    ): ResponseEntity<UserResponseDto> = ResponseEntity.ok(userService.findById(id.toLong()))

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<UserResponseDto>> = ResponseEntity.ok(userService.findAll())
}
