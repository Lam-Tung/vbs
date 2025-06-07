package org.lamiey.resource

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.lamiey.dto.ErrorResponseDTO
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
        id = 1,
        vin = "WAUVFAFH0AN008060",
        licensePlate = "A-A-1111",
        name = "Audi A5",
        manufacturer = "Audi",
        model = "A5"
    )

    @Test
    fun test_getVehicles_success() {
        val mockVehicles = getMockVehicles()

        Mockito.`when`(vehicleService.getVehicles())
            .thenReturn(Uni.createFrom().item(mockVehicles))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("size()", equalTo(mockVehicles.size))
            .body("[0].id", equalTo(1))
            .body("[0].vin", equalTo(mockVehicles[0].vin))
            .body("[0].licensePlate", equalTo(mockVehicles[0].licensePlate))
            .body("[0].name", equalTo(mockVehicles[0].name))
            .body("[0].manufacturer", equalTo(mockVehicles[0].manufacturer))
            .body("[0].model", equalTo(mockVehicles[0].model))
            .body("[0].created", equalTo("2025-04-25T08:14:59.666"))
            .body("[0].updated", equalTo("2025-04-25T08:14:59.666"))
            .body("[1].id", equalTo(2))
            .body("[1].vin", equalTo(mockVehicles[1].vin))
            .body("[1].licensePlate", equalTo(mockVehicles[1].licensePlate))
            .body("[1].name", equalTo(mockVehicles[1].name))
            .body("[1].manufacturer", equalTo(mockVehicles[1].manufacturer))
            .body("[1].model", equalTo(mockVehicles[1].model))
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
    fun test_getVehiclesByPage_success() {
        val mockVehiclesPage = getMockVehicles()

        Mockito.`when`(vehicleService.getVehiclesByPage(0, 5))
            .thenReturn(Uni.createFrom().item(mockVehiclesPage))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle/page?pageNumber=0&pageSize=5")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("size()", equalTo(mockVehiclesPage.size))
            .body("[0].id", equalTo(1))
            .body("[0].vin", equalTo(mockVehiclesPage[0].vin))
            .body("[0].licensePlate", equalTo(mockVehiclesPage[0].licensePlate))
            .body("[0].name", equalTo(mockVehiclesPage[0].name))
            .body("[0].manufacturer", equalTo(mockVehiclesPage[0].manufacturer))
            .body("[0].model", equalTo(mockVehiclesPage[0].model))
            .body("[0].created", equalTo("2025-04-25T08:14:59.666"))
            .body("[0].updated", equalTo("2025-04-25T08:14:59.666"))
            .body("[1].id", equalTo(2))
            .body("[1].vin", equalTo(mockVehiclesPage[1].vin))
            .body("[1].licensePlate", equalTo(mockVehiclesPage[1].licensePlate))
            .body("[1].name", equalTo(mockVehiclesPage[1].name))
            .body("[1].manufacturer", equalTo(mockVehiclesPage[1].manufacturer))
            .body("[1].model", equalTo(mockVehiclesPage[1].model))
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
    fun test_getVehicleById_success() {
        val mockVehicle = getMockVehicle()

        Mockito.`when`(vehicleService.getVehicleById(1))
            .thenReturn(Uni.createFrom().item(mockVehicle))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/vehicle/1")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("id", equalTo(1))
            .body("vin", equalTo(mockVehicle.vin))
            .body("licensePlate", equalTo(mockVehicle.licensePlate))
            .body("name", equalTo(mockVehicle.name))
            .body("manufacturer", equalTo(mockVehicle.manufacturer))
            .body("model", equalTo(mockVehicle.model))
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
            .get("/vehicle/1")
            .then()
            .statusCode(Response.Status.NO_CONTENT.statusCode)
    }

    @Test
    fun test_createVehicle_success() {
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
            .body("vin", equalTo(mockVehicle.vin))
            .body("licensePlate", equalTo(mockVehicle.licensePlate))
            .body("name", equalTo(mockVehicle.name))
            .body("manufacturer", equalTo(mockVehicle.manufacturer))
            .body("model", equalTo(mockVehicle.model))
            .body("created", equalTo("2025-04-25T08:14:59.666"))
            .body("updated", equalTo("2025-04-25T08:14:59.666"))
    }

    @Test
    fun test_createVehicle_badRequest_shortVin() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.vin = "POE"

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Invalid VIN: ${mockVehicleDTO.vin}"))
                    .build()
            )
        ).`when`(vehicleService).createVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.createVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.BAD_REQUEST.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Invalid VIN: ${mockVehicleDTO.vin}")
    }

    @Test
    fun test_createVehicle_badRequest_longVin() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.vin = "ASD1244fASDFGHH11111323GGGSS"

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Invalid VIN: ${mockVehicleDTO.vin}"))
                    .build()
            )
        ).`when`(vehicleService).createVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.createVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.BAD_REQUEST.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Invalid VIN: ${mockVehicleDTO.vin}")
    }

    @Test
    fun test_createVehicle_badRequest_missingVin() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.vin = null

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Request is missing VIN"))
                    .build()
            )
        ).`when`(vehicleService).createVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.createVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.BAD_REQUEST.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Request is missing VIN")
    }

    @Test
    fun test_createVehicle_badRequest_emptyVin() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.vin = ""

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Invalid VIN: "))
                    .build()
            )
        ).`when`(vehicleService).createVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.createVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.BAD_REQUEST.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Invalid VIN: ")
    }

    @Test
    fun test_updateVehicle_success() {
        val mockVehicleDTO = getMockVehicleDTO()
        val updatedVehicle = Vehicle().apply {
            vin = mockVehicleDTO.vin
            licensePlate = mockVehicleDTO.licensePlate?.trim()
            name = mockVehicleDTO.name?.trim()
            manufacturer = mockVehicleDTO.manufacturer?.trim()
            model = mockVehicleDTO.model?.trim()
            updated = LocalDateTime.parse("2025-04-26 08:14:59.666", formatter)
        }

        Mockito.`when`(vehicleService.updateVehicle(mockVehicleDTO))
            .thenReturn(Uni.createFrom().item(updatedVehicle))

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(mockVehicleDTO)
            .`when`()
            .put("/vehicle")
            .then()
            .statusCode(Response.Status.OK.statusCode)
            .body("vin", equalTo(updatedVehicle.vin))
            .body("licensePlate", equalTo(updatedVehicle.licensePlate))
            .body("name", equalTo(updatedVehicle.name))
            .body("manufacturer", equalTo(updatedVehicle.manufacturer))
            .body("model", equalTo(updatedVehicle.model))
            .body("updated", equalTo("2025-04-26T08:14:59.666"))
    }

    @Test
    fun test_updateVehicle_badRequest_missingId() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.id = null

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Request is missing ID"))
                    .build()
            )
        ).`when`(vehicleService).updateVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.updateVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.BAD_REQUEST.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Request is missing ID")
    }

    @Test
    fun test_updateVehicle_notFound() {
        val mockVehicleDTO = getMockVehicleDTO()

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ErrorResponseDTO("Vehicle with ID ${mockVehicleDTO.id} not found"))
                    .build()
            )
        ).`when`(vehicleService).updateVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.updateVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.NOT_FOUND.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Vehicle with ID ${mockVehicleDTO.id} not found")
    }

    @Test
    fun test_deleteVehicle_success() {
        val mockVehicleDTO = getMockVehicleDTO()

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.NO_CONTENT)
                    .build()
            )
        ).`when`(vehicleService).deleteVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.deleteVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.NO_CONTENT.statusCode)
    }

    @Test
    fun test_deleteVehicle_badRequest_missingId() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.id = null

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponseDTO("Request is missing ID"))
                    .build()
            )
        ).`when`(vehicleService).deleteVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.deleteVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.BAD_REQUEST.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Request is missing ID")
    }

    @Test
    fun test_deleteVehicle_notFound() {
        val mockVehicleDTO = getMockVehicleDTO()

        Mockito.doThrow(
            WebApplicationException(
                Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(ErrorResponseDTO("Vehicle with ID ${mockVehicleDTO.id} not found"))
                    .build()
            )
        ).`when`(vehicleService).deleteVehicle(mockVehicleDTO)

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.deleteVehicle(mockVehicleDTO)
        }

        assert(exception.response.status == Response.Status.NOT_FOUND.statusCode)
        assert(exception.response.entity is ErrorResponseDTO)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assert(errorResponse.message == "Vehicle with ID ${mockVehicleDTO.id} not found")
    }
}