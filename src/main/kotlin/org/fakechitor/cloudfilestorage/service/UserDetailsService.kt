package org.fakechitor.cloudfilestorage.service

import org.fakechitor.cloudfilestorage.repository.CustomRepository
import org.fakechitor.cloudfilestorage.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val userRepository: UserRepository,
    private val customRepository: CustomRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = username?.let { userRepository.findByLogin(it) } ?: throw UsernameNotFoundException("User not found")
        return User(
            user.login,
            user.password,
            getGrantedAuthorities(user.login),
        )
    }

    private fun getGrantedAuthorities(username: String?): List<GrantedAuthority> =
        customRepository.getAuthoritiesByName(username).map {
            SimpleGrantedAuthority(it)
        }
}
