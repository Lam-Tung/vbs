package org.lamiey.resource

import io.smallrye.mutiny.Uni
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.service.VehicleService

@Path("/api/vehicle")
class VehicleResource(private val vehicleService: VehicleService) {
    // region GETTER
    @GET
    @Path("/page")
    fun getVehicles(
            @RestQuery("pageNumber") @DefaultValue("0") pageNumber: Int,
            @RestQuery("pageSize") @DefaultValue("10") pageSize: Int
    ): Uni<List<Vehicle>> = vehicleService.getVehicles(pageNumber, pageSize)

    @GET
    @Path("/id/{id}")
    fun getVehicleById(@RestPath("id") @DefaultValue("1") id: Long): Uni<Vehicle> =
        vehicleService.getVehicleById(id)


    // endregion

    // region CRUD
    @POST
    fun createVehicle(vehicleDTO: VehicleDTO): Uni<Response> = vehicleService.createVehicle(vehicleDTO)
        .onItem().transform { createdVehicle ->
            // If the vehicle is created successfully, return a 201 Created response
            Response.status(Response.Status.CREATED).entity(createdVehicle).build()
        }
        .onFailure().recoverWithItem { throwable ->
            // Handle different types of exceptions and return appropriate responses
            when (throwable) {
                is WebApplicationException -> {
                    throwable.response
                }
                else -> {
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred: ${throwable.message}")
                        .build()
                }
            }
        }

    @PUT
    fun updateVehicle(vehicleDTO: VehicleDTO): Uni<Response> = vehicleService.updateVehicle(vehicleDTO)
        .onItem().transform { updatedVehicle ->
            Response.ok(updatedVehicle).build()
        }

//    @DELETE
//    fun deleteVehicle(vehicleDTO: VehicleDTO): Response {
//        try {
//            vehicleService.deleteVehicle(vehicleDTO)
//            return Response.noContent().build()
//        } catch (e: WebApplicationException) {
//            return e.response
//        }
//    }
    // endregion
}
