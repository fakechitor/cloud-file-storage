package org.fakechitor.cloudfilestorage.repository

import org.fakechitor.cloudfilestorage.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}
