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
        licensePlate = "A-A-1111",
        name = "Audi A5",
        manufacturer = "Audi",
        model = "A5"
    )

    private fun getMockVehicle(): Vehicle = Vehicle().apply {
        id = 1
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
            licensePlate = "A-A-1111"
            name = "Audi A5"
            manufacturer = "Audi"
            model = "A5"
            created = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
            updated = LocalDateTime.parse("2025-04-25 08:14:59.666", formatter)
        },
        Vehicle().apply {
            id = 2
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
    //endregion
}