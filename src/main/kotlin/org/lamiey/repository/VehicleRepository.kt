package org.lamiey.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.Vehicle

@ApplicationScoped
class VehicleRepository : PanacheRepository<Vehicle> {
    fun getAllVehicles(pageNumber: Int, pageSize: Int): Uni<List<Vehicle>> =
            findAll().page(Page.of(pageNumber, pageSize)).list()

    fun getByVin(vin: String): Uni<Vehicle?> = find("vin", vin).firstResult()
}
