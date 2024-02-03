package org.lamiey.resource

import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestPath
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.service.VehicleService

@Path("/api/vehicle")
class VehicleResource(
    private val vehicleService: VehicleService
) {
    //region GETTER
    @GET
    @Path("/page/{pageNumber}")
    fun getVehicles(@RestPath("pageNumber") pageNumber: Int): Response {
        try {
            val vehicles: List<Vehicle> = vehicleService.getVehicles(pageNumber)
            return Response.ok(vehicles).build()
        } catch (e: WebApplicationException) {
            return e.response
        }
    }

    @GET
    @Path("/id/{id}")
    fun getVehicleById(@RestPath("id") id: Long): Response {
        try {
            val vehicle: Vehicle? = vehicleService.getVehicleById(id)
            return Response.ok(vehicle).build()
        } catch (e: WebApplicationException) {
            return e.response
        }
    }
    //endregion

    //region CRUD
    @POST
    fun createVehicle(vehicleDTO: VehicleDTO): Response {
        try {
            val vehicle: Vehicle = vehicleService.createVehicle(vehicleDTO)
            return Response.ok(vehicle).build()
        } catch (e: WebApplicationException) {
            return e.response
        }
    }

    @PUT
    fun updateVehicle(vehicleDTO: VehicleDTO): Response {
        try {
            val vehicle: Vehicle = vehicleService.updateVehicle(vehicleDTO)
            return Response.ok(vehicle).build()
        } catch (e: WebApplicationException) {
            return e.response
        }
    }

    @DELETE
    fun deleteVehicle(vehicleDTO: VehicleDTO): Response {
        try {
            vehicleService.deleteVehicle(vehicleDTO)
            return Response.noContent().build()
        } catch (e: WebApplicationException) {
            return e.response
        }
    }
    //endregion
}