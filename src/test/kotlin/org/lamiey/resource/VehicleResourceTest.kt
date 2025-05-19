package org.lamiey.resource

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.service.VehicleService
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@QuarkusTest
class VehicleResourceTest {
    @InjectMock
    lateinit var vehicleService: VehicleService

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    private fun getMockVehicle(): Vehicle = Vehicle().apply {
        id = 1
        vin = "WAUVFAFH0AN008060"
        licensePlate = "A-A-1111"
        name = "Audi A5"
        manufacturer = "Audi"
        model = "A5"
        created = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
        updated = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
    }

    private fun getMockVehicles(): List<Vehicle> = listOf(
        Vehicle().apply {
            id = 1
            vin = "WAUVFAFH0AN008060"
            licensePlate = "A-A-1111"
            name = "Audi A5"
            manufacturer = "Audi"
            model = "A5"
            created = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
            updated = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
        },
        Vehicle().apply {
            id = 2
            vin = "WBAEP33403PE91635"
            licensePlate = "B-B-2222"
            name = "BMW 3 Series"
            manufacturer = "BMW"
            model = "3 Series"
            created = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
            updated = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
        },
    )

    private fun getMockVehicleDTO(): VehicleDTO = VehicleDTO(
        vin = "WAUVFAFH0AN008060",
        licensePlate = "A-A-1111",
        name = "Audi A5",
        manufacturer = "Audi",
        model = "A5"
    )

    @Test
    fun test_getVehicles_ok() {
        val mockVehicles = getMockVehicles()

        Mockito.`when`(vehicleService.getVehicles())
            .thenReturn(Uni.createFrom().item(mockVehicles))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("size()", equalTo(2))
            .body("[0].id", equalTo(1))
            .body("[0].vin", equalTo("WAUVFAFH0AN008060"))
            .body("[0].licensePlate", equalTo("A-A-1111"))
            .body("[0].name", equalTo("Audi A5"))
            .body("[0].manufacturer", equalTo("Audi"))
            .body("[0].model", equalTo("A5"))
            .body("[0].created", equalTo("2025-04-25T08:14:59.666"))
            .body("[0].updated", equalTo("2025-04-25T08:14:59.666"))
            .body("[1].id", equalTo(2))
            .body("[1].vin", equalTo("WBAEP33403PE91635"))
            .body("[1].licensePlate", equalTo("B-B-2222"))
            .body("[1].name", equalTo("BMW 3 Series"))
            .body("[1].manufacturer", equalTo("BMW"))
            .body("[1].model", equalTo("3 Series"))
            .body("[1].created", equalTo("2025-04-25T08:14:59.666"))
            .body("[1].updated", equalTo("2025-04-25T08:14:59.666"))
    }

    @Test
    fun test_getVehicles_empty() {
        val mockVehicles = emptyList<Vehicle>()

        Mockito.`when`(vehicleService.getVehicles())
            .thenReturn(Uni.createFrom().item(mockVehicles))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("size()", equalTo(0))
    }

    @Test
    fun test_getVehiclesByPage_ok() {
        val mockVehiclesPage = getMockVehicles()

        Mockito.`when`(vehicleService.getVehiclesByPage(0, 5))
            .thenReturn(Uni.createFrom().item(mockVehiclesPage))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle/page?pageNumber=0&pageSize=5")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("size()", equalTo(2))
            .body("[0].id", equalTo(1))
            .body("[0].vin", equalTo("WAUVFAFH0AN008060"))
            .body("[0].licensePlate", equalTo("A-A-1111"))
            .body("[0].name", equalTo("Audi A5"))
            .body("[0].manufacturer", equalTo("Audi"))
            .body("[0].model", equalTo("A5"))
            .body("[0].created", equalTo("2025-04-25T08:14:59.666"))
            .body("[0].updated", equalTo("2025-04-25T08:14:59.666"))
            .body("[1].id", equalTo(2))
            .body("[1].vin", equalTo("WBAEP33403PE91635"))
            .body("[1].licensePlate", equalTo("B-B-2222"))
            .body("[1].name", equalTo("BMW 3 Series"))
            .body("[1].manufacturer", equalTo("BMW"))
            .body("[1].model", equalTo("3 Series"))
            .body("[1].created", equalTo("2025-04-25T08:14:59.666"))
            .body("[1].updated", equalTo("2025-04-25T08:14:59.666"))
    }

    @Test
    fun test_getVehiclesByPage_empty() {
        val mockVehiclesPage = emptyList<Vehicle>()

        Mockito.`when`(vehicleService.getVehiclesByPage(0, 5))
            .thenReturn(Uni.createFrom().item(mockVehiclesPage))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle/page?pageNumber=0&pageSize=5")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("size()", equalTo(0))
    }

    @Test
    fun test_getVehicleById_ok() {
        val mockVehicle = getMockVehicle()

        Mockito.`when`(vehicleService.getVehicleById(1))
            .thenReturn(Uni.createFrom().item(mockVehicle))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle/id/1")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("id", equalTo(1))
            .body("vin", equalTo("WAUVFAFH0AN008060"))
            .body("licensePlate", equalTo("A-A-1111"))
            .body("name", equalTo("Audi A5"))
            .body("manufacturer", equalTo("Audi"))
            .body("model", equalTo("A5"))
            .body("created", equalTo("2025-04-25T08:14:59.666"))
            .body("updated", equalTo("2025-04-25T08:14:59.666"))
    }

    @Test
    fun test_getVehicleById_noContent() {
        Mockito.`when`(vehicleService.getVehicleById(1))
            .thenThrow(
                WebApplicationException(
                    Response.noContent().build()
                )
            )

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle/id/1")
            .then()
            .statusCode(Response.Status.NO_CONTENT.statusCode)
    }

    @Test
    fun test_createVehicle_ok() {
        val mockVehicle = getMockVehicle()
        val mockVehicleDTO = getMockVehicleDTO()

        Mockito.`when`(vehicleService.createVehicle(mockVehicleDTO))
            .thenReturn(Uni.createFrom().item(mockVehicle))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(mockVehicleDTO)
            .`when`()
            .post("/vehicle")
            .then()
            .statusCode(Response.Status.CREATED.statusCode)
            .body("id", equalTo(1))
            .body("vin", equalTo("WAUVFAFH0AN008060"))
            .body("licensePlate", equalTo("A-A-1111"))
            .body("name", equalTo("Audi A5"))
            .body("manufacturer", equalTo("Audi"))
            .body("model", equalTo("A5"))
            .body("created", equalTo("2025-04-25T08:14:59.666"))
            .body("updated", equalTo("2025-04-25T08:14:59.666"))
    }
}