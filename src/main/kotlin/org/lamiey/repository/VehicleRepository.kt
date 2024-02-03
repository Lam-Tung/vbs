package org.lamiey.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import jakarta.enterprise.context.ApplicationScoped
import org.lamiey.entity.Vehicle

@ApplicationScoped
class VehicleRepository : PanacheRepository<Vehicle> {
    fun getAllVehicles(pageNumber: Int): List<Vehicle> = findAll().page(Page.of(pageNumber, 10)).list()

}