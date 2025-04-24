package org.lamiey.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.eclipse.microprofile.openapi.annotations.media.Schema
import java.time.LocalDateTime

data class BookingDTO(
    @JsonProperty("id")
    @field:Schema(description = "The unique identifier of the booking", defaultValue = "1")
    var id: Long? = null,

    @JsonProperty("vehicleId")
    @field:Schema(description = "The unique identifier of the vehicle", defaultValue = "1")
    var vehicleId: Long? = null,

    @JsonProperty("userId")
    @field:Schema(description = "The unique identifier of the user", defaultValue = "1")
    var userId: Long? = null,

    @JsonProperty("startDate")
    @field:Schema(description = "The start date of the booking", defaultValue = "2025-05-01T10:00:00")
    var startDate: LocalDateTime? = null,

    @JsonProperty("endDate")
    @field:Schema(description = "The end date of the booking", defaultValue = "2025-05-02T12:00:00")
    var endDate: LocalDateTime? = null,
)
