package org.lamiey.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.lamiey.dto.BookingDTO
import org.lamiey.dto.ErrorResponseDTO
import org.lamiey.entity.Booking
import org.lamiey.repository.BookingRepository

@ApplicationScoped
@WithSession
@WithTransaction
class BookingService @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val vehicleService: VehicleService,
    private val userService: UserService
) {
    //region GETTER
    fun getBookingsByPage(pageNumber: Int, pageSize: Int): Uni<List<Booking>> =
        bookingRepository.getBookingsByPage(pageNumber, pageSize)

    fun getBookingById(id: Long): Uni<Booking> = bookingRepository.findById(id)
    //endregion

    //region CRUD
    fun createBooking(bookingDTO: BookingDTO): Uni<Booking> {
        val vehicleId = retrieveVehicleIdFromDTO(bookingDTO)
        val userId = retrieveUserIdFromDTO(bookingDTO)
        validateBookingDates(bookingDTO)

        val vehicleUni = vehicleService.getVehicleById(vehicleId)
        val userUni = userService.getUserById(userId)

//        return Uni.combine().all().unis(
//            vehicleUni, userUni
//        )
//            .with { vehicleEntity, userEntity ->
//                // Create the Booking object after both entities have been fetched
//                val booking = Booking().apply {
//                    this.vehicle = vehicleEntity
//                    this.user = userEntity
//                    this.startDate = bookingDTO.startDate
//                    this.endDate = bookingDTO.endDate
//                }
//
//                // Persist the booking
//                bookingRepository.persist(booking)
//
//                // Return the booking
//                booking
//            }
        return TODO()
    }

    fun deleteBooking(bookingDTO: BookingDTO): Uni<Void> {
        val bookingId = retrieveBookingIdFromDTO(bookingDTO)

        return bookingRepository.findById(bookingId)
            .onItem().transformToUni { booking ->
                if (booking == null) {
                    throw WebApplicationException(
                        Response.noContent().build()
                    )
                }

                bookingRepository.delete(booking)
            }
    }
    //endregion

    //region RETRIEVE VALUE FROM DTO
    private fun retrieveBookingIdFromDTO(bookingDTO: BookingDTO): Long = bookingDTO.id
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request missing ID")
                .build()
        )

    private fun retrieveVehicleIdFromDTO(bookingDTO: BookingDTO): Long = bookingDTO.vehicleId
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request is missing vehicleId")
                .build()
        )

    private fun retrieveUserIdFromDTO(bookingDTO: BookingDTO): Long = bookingDTO.userId
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request is missing userId")
                .build()
        )
    //endregion

    private fun validateBookingDates(bookingDTO: BookingDTO) {
        val startDate = bookingDTO.startDate
            ?: throw WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Request is missing start date"))
                    .build()
            )

        val endDate = bookingDTO.endDate
            ?: throw WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Request is missing end date"))
                    .build()
            )

        if (startDate.isAfter(endDate)) {
            throw WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("End date must be after start date"))
                    .build()
            )
        }
    }
}