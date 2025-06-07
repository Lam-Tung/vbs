package org.lamiey.service

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheQuery
import io.quarkus.test.InjectMock
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.lamiey.dto.ErrorResponseDTO
import org.lamiey.dto.VehicleDTO
import org.lamiey.entity.Vehicle
import org.lamiey.repository.VehicleRepository
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@QuarkusTest
class VehicleServiceTest {
    //region Injects
    @InjectMock
    lateinit var vehicleRepository: VehicleRepository
    //endregion

    @Inject
    lateinit var vehicleService: VehicleService

    //region Helper functions
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    private fun getMockVehicleDTO(): VehicleDTO = VehicleDTO(
        id = 1,
        vin = "WAUVFAFH0AN008060",
        licensePlate = "A-A-1111",
        name = "Audi A5",
        manufacturer = "Audi",
        model = "A5"
    )

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

    private fun getMockVin(): String = getMockVehicle().vin!!

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
    //endregion

    //region Tests
    @Test
    @RunOnVertxContext
    fun test_getVehicles_success(asserter: TransactionalUniAsserter) {
        val mockVehicles = getMockVehicles()
        val mockQuery = Mockito.mock(PanacheQuery::class.java) as PanacheQuery<Vehicle>

        Mockito.`when`(vehicleRepository.findAll()).thenReturn(mockQuery)
        Mockito.`when`(mockQuery.list()).thenReturn(Uni.createFrom().item(mockVehicles))

        asserter.assertEquals({ vehicleService.getVehicles() }, mockVehicles)
    }

    @Test
    @RunOnVertxContext
    fun test_getVehicles_empty(asserter: TransactionalUniAsserter) {
        val mockVehiclesEmpty = listOf<Vehicle>()
        val mockQuery = Mockito.mock(PanacheQuery::class.java) as PanacheQuery<Vehicle>

        Mockito.`when`(vehicleRepository.findAll()).thenReturn(mockQuery)
        Mockito.`when`(mockQuery.list()).thenReturn(Uni.createFrom().item(mockVehiclesEmpty))
        asserter.assertEquals({ vehicleService.getVehicles() }, mockVehiclesEmpty)
    }

    @Test
    @RunOnVertxContext
    fun test_getVehiclesByPage_success(asserter: TransactionalUniAsserter) {
        val mockVehicles = getMockVehicles()
        val pageNumber = 0
        val pageSize = 10

        Mockito.`when`(vehicleRepository.getVehiclesByPage(pageNumber, pageSize))
            .thenReturn(Uni.createFrom().item(mockVehicles))

        asserter.assertEquals(
            { vehicleService.getVehiclesByPage(pageNumber, pageSize) },
            mockVehicles
        )
    }

    @Test
    @RunOnVertxContext
    fun test_getVehiclesByPage_empty(asserter: TransactionalUniAsserter) {
        val mockVehiclesEmpty = listOf<Vehicle>()
        val pageNumber = 0
        val pageSize = 10

        Mockito.`when`(vehicleRepository.getVehiclesByPage(pageNumber, pageSize))
            .thenReturn(Uni.createFrom().item(mockVehiclesEmpty))

        asserter.assertEquals(
            { vehicleService.getVehiclesByPage(pageNumber, pageSize) },
            mockVehiclesEmpty
        )
    }

    @Test
    @RunOnVertxContext
    fun test_getVehiclesByPage_secondPage(asserter: TransactionalUniAsserter) {
        val mockVehicles = getMockVehicles()
        mockVehicles.drop(1)
        val pageNumber = 1
        val pageSize = 1

        Mockito.`when`(vehicleRepository.getVehiclesByPage(pageNumber, pageSize))
            .thenReturn(Uni.createFrom().item(mockVehicles))

        asserter.assertEquals(
            { vehicleService.getVehiclesByPage(pageNumber, pageSize) },
            mockVehicles
        )
    }

    @Test
    @RunOnVertxContext
    fun test_getVehicleById_success(asserter: TransactionalUniAsserter) {
        val mockVehicle = getMockVehicle()

        Mockito.`when`(vehicleRepository.findById(1))
            .thenReturn(Uni.createFrom().item(mockVehicle))

        asserter.assertEquals({ vehicleService.getVehicleById(1) }, mockVehicle)
    }

    @Test
    @RunOnVertxContext
    fun test_getVehicleById_notFound(asserter: TransactionalUniAsserter) {
        Mockito.`when`(vehicleRepository.findById(1))
            .thenReturn(Uni.createFrom().nullItem())

        asserter.assertEquals({ vehicleService.getVehicleById(1) }, null)
    }

    @Test
    fun test_retrieveVehicleIdFromDTO_success() {
        val mockVehicleDTO = getMockVehicleDTO()
        val vehicleId = vehicleService.retrieveVehicleIdFromDTO(mockVehicleDTO)

        assertEquals(1, vehicleId)
    }

    @Test
    fun test_retrieveVehicleIdFromDTO_missingId() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.id = null

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.retrieveVehicleIdFromDTO(mockVehicleDTO)
        }
        assertEquals(Response.Status.BAD_REQUEST.statusCode, exception.response.status)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assertEquals("Request is missing ID", errorResponse.message)
    }

    @Test
    fun test_retrieveVehicleVinFromDTO_success() {
        val mockVehicleDTO = getMockVehicleDTO()
        val vin = vehicleService.retrieveVehicleVinFromDTO(mockVehicleDTO)

        assertEquals(getMockVin(), vin)
    }

    @Test
    fun test_retrieveVehicleVinFromDTO_missingVin() {
        val mockVehicleDTO = getMockVehicleDTO()
        mockVehicleDTO.vin = null

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.retrieveVehicleVinFromDTO(mockVehicleDTO)
        }
        assertEquals(Response.Status.BAD_REQUEST.statusCode, exception.response.status)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assertEquals("Request is missing VIN", errorResponse.message)
    }

    @Test
    fun test_validateVinFormat_validVin() {
        val validVin = getMockVin()

        vehicleService.validateVinFormat(validVin)
    }

    @Test
    fun test_validateVinFormat_emptyVin() {
        val emptyVin = ""

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.validateVinFormat(emptyVin)
        }
        assertEquals(Response.Status.BAD_REQUEST.statusCode, exception.response.status)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assertEquals("Invalid VIN: $emptyVin", errorResponse.message)
    }

    @Test
    fun test_validateVinFormat_shortVin() {
        val shortVin = "WAUVFAF"

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.validateVinFormat(shortVin)
        }
        assertEquals(Response.Status.BAD_REQUEST.statusCode, exception.response.status)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assertEquals("Invalid VIN: $shortVin", errorResponse.message)
    }

    @Test
    fun test_validateVinFormat_longVin() {
        val longVin = "WAUVFAFH0AN0080601"

        val exception = assertThrows(WebApplicationException::class.java) {
            vehicleService.validateVinFormat(longVin)
        }
        assertEquals(Response.Status.BAD_REQUEST.statusCode, exception.response.status)
        val errorResponse = exception.response.entity as ErrorResponseDTO
        assertEquals("Invalid VIN: $longVin", errorResponse.message)
    }

    @Test
    @RunOnVertxContext
    fun test_vinAlreadyExist_success(asserter: TransactionalUniAsserter) {
        val vin = getMockVin()

        Mockito.`when`(vehicleRepository.getByVin(vin))
            .thenReturn(Uni.createFrom().nullItem())

        val result = vehicleService.vinAlreadyExists(vin)
        asserter.assertFalse { result }
    }

    @Test
    @RunOnVertxContext
    fun test_vinAlreadyExist_vinExists(asserter: TransactionalUniAsserter) {
        val vin = getMockVin()
        val mockVehicle = getMockVehicle()

        Mockito.`when`(vehicleRepository.getByVin(vin))
            .thenReturn(Uni.createFrom().item(mockVehicle))

        asserter.assertFailedWith(
            { vehicleService.vinAlreadyExists(vin) },
            WebApplicationException::class.java
        )
    }
    //endregion
}