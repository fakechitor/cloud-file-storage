package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.model.Role
import org.fakechitor.cloudfilestorage.repository.RoleRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoleService(
    private val roleRepository: RoleRepository,
) {
    @Transactional
    @Cacheable("roles")
    fun findByName(name: String): Role? = roleRepository.findByName(name)
}
