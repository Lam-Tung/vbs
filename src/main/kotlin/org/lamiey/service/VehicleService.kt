package org.lamiey.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.repository.VehicleRepository

@ApplicationScoped
class VehicleService(private val vehicleRepository: VehicleRepository) {
    // region GETTER
    fun getVehicles(pageNumber: Int, pageSize: Int): List<Vehicle> =
            vehicleRepository.getAllVehicles(pageNumber, pageSize)

    fun getVehicleById(id: Long): Vehicle? = vehicleRepository.findById(id)
    // endregion

    // region CRUD
    fun createVehicle(vehicleDTO: VehicleDTO): Vehicle {
        val vehicle =
                Vehicle().apply {
                    licensePlate = vehicleDTO.licensePlate
                    name = vehicleDTO.name
                    manufacturer = vehicleDTO.manufacturer
                    model = vehicleDTO.model
                }

        vehicleRepository.persist(vehicle)

        return vehicle
    }

    fun updateVehicle(vehicleDTO: VehicleDTO): Vehicle {
        val vehicle: Vehicle? = vehicleDTO.id?.let { getVehicleById(it) }

        if (vehicle == null)
                throw WebApplicationException(Response.status(Response.Status.NOT_FOUND).build())

        vehicleRepository.persist(
                vehicle.apply {
                    licensePlate = vehicleDTO.licensePlate
                    name = vehicleDTO.name
                    manufacturer = vehicleDTO.manufacturer
                    model = vehicleDTO.model
                }
        )

        return vehicle
    }

    fun deleteVehicle(vehicleDTO: VehicleDTO) {
        val vehicle: Vehicle? = vehicleDTO.id?.let { vehicleRepository.findById(it) }

        if (vehicle == null)
                throw WebApplicationException(Response.status(Response.Status.NOT_FOUND).build())

        vehicleRepository.delete(vehicle)
    }
    // endregion
}
