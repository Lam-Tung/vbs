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
        val vehicleVin = retrieveVehicleVinFromDTO(vehicleDTO)
        validateVin(vehicleVin)

        return vinAlreadyExists(vehicleVin)
            .onItem().transform {
                Vehicle().apply {
                    vin = vehicleVin
                    licensePlate = vehicleDTO.licensePlate?.trim()
                    name = vehicleDTO.name?.trim()
                    manufacturer = vehicleDTO.manufacturer?.trim()
                    model = vehicleDTO.model?.trim()
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
            val vehicleId = retrieveVehicleIdFromDTO(vehicleDTO)

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
                        licensePlate = vehicleDTO.licensePlate?.trim()
                        name = vehicleDTO.name?.trim()
                        manufacturer = vehicleDTO.manufacturer?.trim()
                        model = vehicleDTO.model?.trim()
                    }
                }
    }

    fun deleteVehicle(vehicleDTO: VehicleDTO): Uni<Void> {
        val vehicleId = retrieveVehicleIdFromDTO(vehicleDTO)

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

    //region RETRIEVE VALUES FROM DTO
    private fun retrieveVehicleIdFromDTO(vehicleDTO: VehicleDTO): Long = vehicleDTO.id
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request missing ID")
                .build()
        )

    private fun retrieveVehicleVinFromDTO(vehicleDTO: VehicleDTO): String = vehicleDTO.vin?.trim()
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request missing VIN")
                .build()
        )
    //endregion

    //region CHECKS
    private fun validateVin(vin: String) {
        if (vin.isEmpty() || vin.length != 17) {
            throw WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Invalid VIN: $vin"))
                    .build()
            )
        }
    }

    private fun vinAlreadyExists(vin: String): Uni<Boolean> = vehicleRepository.getByVin(vin)
        .onItem().transformToUni { existingVehicle ->
            if (existingVehicle != null) {
                throw WebApplicationException(
                    Response
                        .status(Response.Status.CONFLICT)
                        .entity(ErrorResponseDTO("VIN $vin already exist"))
                        .build()
                )
            }

            Uni.createFrom().item(false)
        }
    //endregion
}
