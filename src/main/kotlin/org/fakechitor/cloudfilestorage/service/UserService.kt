package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.mapper.UserMapper
import org.fakechitor.cloudfilestorage.dto.response.UserResponseDto
import org.fakechitor.cloudfilestorage.exception.UserAlreadyExistsException
import org.fakechitor.cloudfilestorage.model.User
import org.fakechitor.cloudfilestorage.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val securityContextHolder: SecurityContextHolderStrategy,
) {
    fun findAll(): List<UserResponseDto> = userRepository.findAll().map { userMapper.convertToDto(it) }

    fun findById(id: Long): UserResponseDto? = userRepository.findByIdOrNull(id)?.let { userMapper.convertToDto(it) }

    @Transactional
    fun save(user: User): User =
        runCatching { userRepository.save(user) }.fold(
            onSuccess = { it },
            onFailure = { throw UserAlreadyExistsException("User already exists") },
        )

    @Transactional
    fun deleteAll() = userRepository.deleteAll()

    fun getUsernameIfAuthorized(): UserResponseDto {
        val authentication = SecurityContextHolder.getContext().authentication
        return UserResponseDto(authentication?.takeIf { authentication.isAuthenticated }?.name)
    }

    fun getParentFolderNameForUser() = "user-${getIdByName(securityContextHolder.context.authentication.name)}-files/"

    private fun getIdByName(name: String) = userRepository.findByLogin(name)?.id
}
