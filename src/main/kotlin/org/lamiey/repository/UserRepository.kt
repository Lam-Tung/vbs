package org.lamiey.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.User

@ApplicationScoped
class UserRepository : PanacheRepository<User> {
    fun getAllUsers(pageNumber: Int, pageSize: Int): Uni<List<User>> =
        findAll().page(Page.of(pageNumber, pageSize)).list()

    fun getByEmail(email: String): Uni<User?> = find("email", email).firstResult()
}