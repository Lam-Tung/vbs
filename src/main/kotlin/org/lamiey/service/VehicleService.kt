package org.lamiey.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.repository.VehicleRepository

@ApplicationScoped
@WithSession
class VehicleService(private val vehicleRepository: VehicleRepository) {
    private val logger = Logger.getLogger(VehicleService::class.java)

    // region GETTER
    fun getVehicles(pageNumber: Int, pageSize: Int): Uni<List<Vehicle>> =
            vehicleRepository.getAllVehicles(pageNumber, pageSize)


    fun getVehicleById(id: Long): Uni<Vehicle> = vehicleRepository.findById(id)
    // endregion

    // region CRUD
    fun createVehicle(vehicleDTO: VehicleDTO): Uni<Vehicle> {
        val vehicle = Vehicle().apply {
            vin = vehicleDTO.vin.toString()
            licensePlate = vehicleDTO.licensePlate
            name = vehicleDTO.name
            manufacturer = vehicleDTO.manufacturer
            model = vehicleDTO.model
        }

        return vehicleRepository.persist(vehicle)
            .onItem().transform { persistedVehicle ->
                persistedVehicle
            }
            .onFailure().invoke { throwable ->
                logger.error("Failed to create vehicle: $throwable")
                throw throwable
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
                                .entity("Vehicle with ID ${vehicleDTO.id} not found")
                                .build()
                        )
                    }

                    vehicle.apply {
                        vin = vehicleDTO.vin.toString()
                        licensePlate = vehicleDTO.licensePlate
                        name = vehicleDTO.name
                        manufacturer = vehicleDTO.manufacturer
                        model = vehicleDTO.model
                    }

                    vehicle
                }
    }

//    fun deleteVehicle(vehicleDTO: VehicleDTO) {
//        val vehicle: Vehicle? = vehicleDTO.id?.let { vehicleRepository.findById(it) }
//
//        if (vehicle == null)
//                throw WebApplicationException(Response.status(Response.Status.NOT_FOUND).build())
//
//        vehicleRepository.delete(vehicle)
//    }
    // endregion

    fun validateVehicleId(vehicleDTO: VehicleDTO): Long = vehicleDTO.id
        ?: throw WebApplicationException(
            Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Request missing ID")
                .build()
        )

}
