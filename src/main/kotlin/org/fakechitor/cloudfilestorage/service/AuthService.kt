package org.fakechitor.cloudfilestorage.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fakechitor.cloudfilestorage.dto.mapper.UserMapper
import org.fakechitor.cloudfilestorage.dto.request.UserRequestDto
import org.fakechitor.cloudfilestorage.dto.response.UserResponseDto
import org.fakechitor.cloudfilestorage.exception.UserAlreadyExistsException
import org.fakechitor.cloudfilestorage.model.UserRole
import org.fakechitor.cloudfilestorage.repository.UserRoleRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException

@Service
class AuthService(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository,
    private val bcryptPasswordEncoder: BCryptPasswordEncoder,
    private val userMapper: UserMapper,
    private val securityContextHolder: SecurityContextHolderStrategy,
    private val userRoleRepository: UserRoleRepository,
    private val roleService: RoleService,
) {
    fun authenticate(
        userRequestDto: UserRequestDto,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): UserResponseDto {
        try {
            UsernamePasswordAuthenticationToken.unauthenticated(userRequestDto.login, userRequestDto.password).also {
                val auth = authenticationManager.authenticate(it)
                if (auth.isAuthenticated) {
                    val context = securityContextHolder.createEmptyContext()
                    context.authentication = auth
                    securityContextHolder.context = context
                    securityContextRepository.saveContext(context, request, response)
                }
            }
        } catch (e: BadCredentialsException) {
            SecurityContextHolder.clearContext()
            throw BadCredentialsException("Failed to login with username: ${userRequestDto.login}", e)
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return UserResponseDto(login = userRequestDto.login)
    }

    fun register(
        userRequestDto: UserRequestDto,
        request: HttpServletRequest,
    ): UserResponseDto =
        try {
            userService
                .save(
                    userMapper
                        .convertToModel(userRequestDto)
                        .apply { password = bcryptPasswordEncoder.encode(password) },
                ).also {
                    userRoleRepository.save(UserRole(it, roleService.findByName("ROLE_USER")))
                    val authToken = UsernamePasswordAuthenticationToken(userRequestDto.login, userRequestDto.password)
                    val authentication = authenticationManager.authenticate(authToken)
                    val securityContext = SecurityContextHolder.getContext()
                    securityContext.authentication = authentication
                    request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext)
                }.let { userMapper.convertToDto(it) }
        } catch (e: DataIntegrityViolationException) {
            throw UserAlreadyExistsException("User already exists")
        } catch (e: Exception) {
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
}
