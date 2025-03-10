package org.fakechitor.cloudfilestorage.controller

import org.fakechitor.cloudfilestorage.docs.users.UserInfoDocs
import org.fakechitor.cloudfilestorage.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
class UserController(
    private val userService: UserService,
) {
    @UserInfoDocs
    @GetMapping("/me")
    fun getPersonalInfo() = ResponseEntity.ok(userService.getUsernameIfAuthorized())
}
