package org.fakechitor.cloudfilestorage.repository

import org.fakechitor.cloudfilestorage.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(username: String): User?
}

fun UserRepository.findByIdOrNull(id: Long): User? = findById(id).orElse(null)
