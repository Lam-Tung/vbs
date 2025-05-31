package org.lamiey.resource

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.service.VehicleService

@Path("/vehicle")
class VehicleResource @Inject constructor(
    private val vehicleService: VehicleService
) {
    // region GETTER
    @GET
    fun getVehicles(): Uni<List<Vehicle>> = vehicleService.getVehicles()

    @GET
    @Path("/page")
    fun getVehiclesByPage(
            @RestQuery("pageNumber") @DefaultValue("0") pageNumber: Int,
            @RestQuery("pageSize") @DefaultValue("10") pageSize: Int
    ): Uni<List<Vehicle>> = vehicleService.getVehiclesByPage(pageNumber, pageSize)

    @GET
    @Path("/{id}")
    fun getVehicleById(@RestPath("id") @DefaultValue("1") id: Long): Uni<Vehicle> =
        vehicleService.getVehicleById(id)
    // endregion

    // region CRUD
    @POST
    @ResponseStatus(201)
    fun createVehicle(vehicleDTO: VehicleDTO): Uni<Vehicle> = vehicleService.createVehicle(vehicleDTO)

    @PUT
    fun updateVehicle(vehicleDTO: VehicleDTO): Uni<Vehicle> = vehicleService.updateVehicle(vehicleDTO)

    @DELETE
    fun deleteVehicle(vehicleDTO: VehicleDTO): Uni<Void> = vehicleService.deleteVehicle(vehicleDTO)
    // endregion
}
