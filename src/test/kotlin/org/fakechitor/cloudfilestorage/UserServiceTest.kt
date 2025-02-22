package org.fakechitor.cloudfilestorage

import org.fakechitor.cloudfilestorage.dto.request.UserRequestDto
import org.fakechitor.cloudfilestorage.exception.UserAlreadyExistsException
import org.fakechitor.cloudfilestorage.service.AuthService
import org.fakechitor.cloudfilestorage.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Profile("test")
class UserServiceTest {
    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    lateinit var userService: UserService

    companion object {
        @Container
        private val postgreSQLContainer =
            PostgreSQLContainer<Nothing>("postgres:17.2").apply {
                withDatabaseName("test")
                withUsername("postgres")
                withPassword("postgres")
            }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

    @AfterEach
    fun clearUserData() {
        userService.deleteAll()
    }

    @Test
    fun testUserSaving() {
        val userReq =
            UserRequestDto(
                login = "tima",
                password = "tima1488",
            )
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        authService.register(userReq, request, response)
        val users = userService.findAll()
        assertNotNull(users)
        assertEquals(1, users.size)
    }

    @Test
    fun testSavingOfExistingUserThrowException() {
        val userReq =
            UserRequestDto(
                login = "artem",
                password = "artem228",
            )
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        authService.register(userReq, request, response)
        assertThrows<UserAlreadyExistsException> {
            authService.register(userReq, request, response)
        }
    }
}
