package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.dto.mapper.UserMapper
import org.fakechitor.cloudfilestorage.dto.response.UserResponseDto
import org.fakechitor.cloudfilestorage.exception.UserAlreadyExistsException
import org.fakechitor.cloudfilestorage.model.User
import org.fakechitor.cloudfilestorage.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<UserResponseDto> = userRepository.findAll().map { userMapper.convertToDto(it) }

    @Transactional(readOnly = true)
    fun findById(id: Long): UserResponseDto? = userRepository.findByIdOrNull(id)?.let { userMapper.convertToDto(it) }

    @Transactional
    fun save(user: User): User =
        runCatching { userRepository.save(user) }.fold(
            onSuccess = { it },
            onFailure = { throw UserAlreadyExistsException("User already exists") },
        )
}
