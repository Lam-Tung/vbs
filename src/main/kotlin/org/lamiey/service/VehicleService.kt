package org.lamiey.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.lamiey.dto.ErrorResponseDTO
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.repository.VehicleRepository

@ApplicationScoped
@WithSession
@WithTransaction
class VehicleService(private val vehicleRepository: VehicleRepository) {
    // region GETTER
    fun getVehicles(pageNumber: Int, pageSize: Int): Uni<List<Vehicle>> =
            vehicleRepository.getAllVehicles(pageNumber, pageSize)


    fun getVehicleById(id: Long): Uni<Vehicle> = vehicleRepository.findById(id)
    // endregion

    // region CRUD
    fun createVehicle(vehicleDTO: VehicleDTO): Uni<Vehicle> {
        val vehicleVin = validateVehicleVin(vehicleDTO)

        return vehicleRepository.getByVin(vehicleVin)
            .onItem().transform { existingVehicle ->
                if (existingVehicle != null) {
                    throw WebApplicationException(
                        Response
                            .status(Response.Status.CONFLICT)
                            .entity(ErrorResponseDTO("VIN $vehicleVin already exist"))
                            .build()
                    )
                }
                Vehicle().apply {
                    vin = vehicleVin
                    licensePlate = vehicleDTO.licensePlate
                    name = vehicleDTO.name
                    manufacturer = vehicleDTO.manufacturer
                    model = vehicleDTO.model
                }
            }
            .onItem().transformToUni { vehicle ->
                vehicleRepository.persist(vehicle)
                    .onItem().transform { persistedVehicle ->
                        persistedVehicle
                    }
            }
    }

    fun updateVehicle(vehicleDTO: VehicleDTO): Uni<Vehicle> {
            val vehicleId = validateVehicleId(vehicleDTO)

            return vehicleRepository.findById(vehicleId)
                .onItem().transform { vehicle ->
                    if (vehicle == null) {
                        throw WebApplicationException(
                            Response
                                .status(Response.Status.NOT_FOUND)
                                .entity(ErrorResponseDTO("Vehicle with ID ${vehicleDTO.id} not found"))
                                .build()
                        )
                    }

                    vehicle.apply {
                        licensePlate = vehicleDTO.licensePlate
                        name = vehicleDTO.name
                        manufacturer = vehicleDTO.manufacturer
                        model = vehicleDTO.model
                    }
                }
    }

    fun deleteVehicle(vehicleDTO: VehicleDTO): Uni<Void> {
        val vehicleId = validateVehicleId(vehicleDTO)

        return vehicleRepository.findById(vehicleId)
            .onItem().transformToUni { vehicle ->
                if (vehicle == null) {
                    throw WebApplicationException(
                        Response.noContent().build()
                    )
                }

                vehicleRepository.delete(vehicle)
            }
    }
    // endregion

    fun validateVehicleId(vehicleDTO: VehicleDTO): Long = vehicleDTO.id
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request missing ID")
                .build()
        )

    fun validateVehicleVin(vehicleDTO: VehicleDTO): String = vehicleDTO.vin
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request missing VIN")
                .build()
        )
}
