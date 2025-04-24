package org.lamiey.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.Booking

@ApplicationScoped
class BookingRepository : PanacheRepository<Booking> {
    fun getBookingsByPage(pageNumber: Int, pageSize: Int): Uni<List<Booking>> =
        findAll().page(Page.of(pageNumber, pageSize)).list()
}