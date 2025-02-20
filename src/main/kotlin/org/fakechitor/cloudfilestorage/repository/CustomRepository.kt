package org.fakechitor.cloudfilestorage.repository

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CustomRepository(
    private val entityManager: EntityManager,
) {
    @Transactional
    fun getAuthoritiesByName(username: String?): List<String> {
        val sql = """
            SELECT role FROM user_roles
                            JOIN public.roles r ON r.id = user_roles.id_role
                            JOIN public.users u ON u.id = user_roles.id_user
                            WHERE login = :username;
        """
        return entityManager
            .createNativeQuery(sql, String::class.java)
            .apply {
                setParameter("username", username)
            }.resultList
            .map { it as String }
    }
}
