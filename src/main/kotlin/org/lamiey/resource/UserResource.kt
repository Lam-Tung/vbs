package org.lamiey.resource

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery
import org.lamiey.dto.UserDTO
import org.lamiey.entity.User
import org.lamiey.service.UserService

@Path("/user")
class UserResource @Inject constructor(
    private val userService: UserService
) {
    // region GETTER
    @GET
    @Path("/page")
    fun getUsers(
        @RestQuery("pageNumber") @DefaultValue("0") pageNumber: Int,
        @RestQuery("pageSize") @DefaultValue("10") pageSize: Int
    ): Uni<List<User>> = userService.getUsers(pageNumber, pageSize)

    @GET
    @Path("/id/{id}")
    fun getUserById(@RestPath("id") @DefaultValue("1") id: Long): Uni<User> =
        userService.getUserById(id)
    // endregion

    // region CRUD
    @POST
    fun createUser(userDTO: UserDTO): Uni<User> = userService.createUser(userDTO)

    @PUT
    fun updateUser(userDTO: UserDTO): Uni<User> = userService.updateUser(userDTO)

    @DELETE
    fun deleteUser(userDTO: UserDTO): Uni<Void> = userService.deleteUser(userDTO)
    // endregion
}