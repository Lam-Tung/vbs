package org.lamiey.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.Booking
import java.time.LocalDateTime

@ApplicationScoped
class BookingRepository : PanacheRepository<Booking> {
    fun getBookingsByPage(pageNumber: Int, pageSize: Int): Uni<List<Booking>> =
        findAll().page(Page.of(pageNumber, pageSize)).list()

    fun getBookingsByVehicleIdInDateRange(
        vehicleId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Uni<List<Booking>> {
        val query = """
            vehicle.id = ?1 AND 
            (
                (startDate >= ?2 AND startDate < ?3) OR 
                (endDate > ?2 AND endDate <= ?3) OR 
                (startDate < ?2 AND endDate > ?3)
            )
        """.trimIndent()

        return find(query, vehicleId, startDate, endDate).list()
    }
}