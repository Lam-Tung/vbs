package org.lamiey.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.User

@ApplicationScoped
class UserRepository : PanacheRepository<User> {
}