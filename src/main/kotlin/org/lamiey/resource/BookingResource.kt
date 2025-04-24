package org.lamiey.resource

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery
import org.lamiey.dto.BookingDTO
import org.lamiey.entity.Booking
import org.lamiey.service.BookingService

@Path("/booking")
class BookingResource @Inject constructor(
    private val bookingService: BookingService
) {
    // region GETTER
    @GET
    @Path("/page")
    fun getBookingsByPage(
        @RestQuery("pageNumber") @DefaultValue("0") pageNumber: Int,
        @RestQuery("pageSize") @DefaultValue("10") pageSize: Int
    ): Uni<List<Booking>> = bookingService.getBookingsByPage(pageNumber, pageSize)

    @GET
    @Path("/id/{id}")
    fun getBookingById(@RestPath("id") @DefaultValue("1") id: Long): Uni<Booking> =
        bookingService.getBookingById(id)
    // endregion

    // region CRUD
    @POST
    fun createBooking(bookingDTO: BookingDTO): Uni<Booking> = bookingService.createBooking(bookingDTO)

//    @PUT
//    fun updateBooking(bookingDTO: BookingDTO): Uni<Booking> = bookingService.updateBooking(bookingDTO)

    @DELETE
    fun deleteBooking(bookingDTO: BookingDTO): Uni<Void> = bookingService.deleteBooking(bookingDTO)
    // endregion
}