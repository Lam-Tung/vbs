package org.lamiey.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.lamiey.dto.ErrorResponseDTO
import org.lamiey.dto.UserDTO
import org.lamiey.entity.User
import org.lamiey.repository.UserRepository

@ApplicationScoped
@WithSession
@WithTransaction
class UserService @Inject constructor(
    private val userRepository: UserRepository
) {
    // region GETTER
    fun getUsersByPage(pageNumber: Int, pageSize: Int): Uni<List<User>> =
        userRepository.getUsersByPage(pageNumber, pageSize)


    fun getUserById(id: Long): Uni<User> = userRepository.findById(id)
    // endregion

    // region CRUD
    fun createUser(userDTO: UserDTO): Uni<User> {
        val userEmail = retrieveEmailFromDTO(userDTO)
        validateEmail(userEmail)

        return checkIfEmailExists(userEmail, null)
            .map {
                User().apply {
                    email = userEmail
                    firstName = userDTO.firstName?.trim()
                    lastName = userDTO.lastName?.trim()
                }
            }
            .chain { user ->
                userRepository.persist(user)
                    .map { persistedUser ->
                        persistedUser
                    }
            }
    }

    fun updateUser(userDTO: UserDTO): Uni<User> {
        val userId = retrieveUserIdFromDTO(userDTO)
        val userEmail = retrieveEmailFromDTO(userDTO)
        validateEmail(userEmail)

        return userRepository.findById(userId)
            .map { existingUser ->
                if (existingUser == null) {
                    throw WebApplicationException(
                        Response
                            .status(Response.Status.NOT_FOUND)
                            .entity(ErrorResponseDTO("User with ID $userId not found"))
                            .build()
                    )
                }

                existingUser
            }
            .call { existingUser ->
                checkIfEmailExists(userEmail, userId)
                    .replaceWith(existingUser)
            }
            .map { existingUser ->
                existingUser.apply {
                    email = userEmail
                    firstName = userDTO.firstName?.trim()
                    lastName = userDTO.lastName?.trim()
                }
            }
    }

    fun deleteUser(userDTO: UserDTO): Uni<Void> {
        val userId = retrieveUserIdFromDTO(userDTO)

        return userRepository.findById(userId)
            .chain { user ->
                if (user == null) {
                    throw WebApplicationException(
                        Response.noContent().build()
                    )
                }

                userRepository.delete(user)
            }
    }
    // endregion

    //region RETRIEVE VALUES FROM DTO
    private fun retrieveUserIdFromDTO(userDTO: UserDTO): Long = userDTO.id
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponseDTO("Request is missing ID"))
                .build()
        )

    private fun retrieveEmailFromDTO(userDTO: UserDTO): String = userDTO.email?.trim()
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponseDTO("Request is missing email"))
                .build()
        )
    //endregion

    //region CHECKS
    private fun validateEmail(email: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        if(!email.matches(Regex(emailRegex))) {
            throw WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Invalid email: $email"))
                    .build()
            )
        }
    }

    private fun checkIfEmailExists(email: String, userId: Long?): Uni<Void> {
        return userRepository.getByEmail(email)
            .map { existingUser ->
                if (existingUser != null && existingUser.id != userId) {
                    throw WebApplicationException(
                        Response
                            .status(Response.Status.CONFLICT)
                            .entity(ErrorResponseDTO("Email $email already exists"))
                            .build()
                    )
                }

                null
            }
            .replaceWith(Uni.createFrom().voidItem())
    }
    //endregion
}