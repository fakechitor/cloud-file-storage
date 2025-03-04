package org.fakechitor.cloudfilestorage.controller

import org.fakechitor.cloudfilestorage.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun getPersonalInfo() = ResponseEntity.ok(userService.getUsernameIfAuthorized())
}
