package org.lamiey.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.Booking

@ApplicationScoped
class BookingRepository : PanacheRepository<Booking> {
}