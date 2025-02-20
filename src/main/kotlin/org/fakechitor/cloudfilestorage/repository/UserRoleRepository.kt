package org.fakechitor.cloudfilestorage.repository

import org.fakechitor.cloudfilestorage.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository : JpaRepository<UserRole, Long>{

}
