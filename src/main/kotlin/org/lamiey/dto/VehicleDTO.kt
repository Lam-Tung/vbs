package org.lamiey.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.eclipse.microprofile.openapi.annotations.media.Schema

data class VehicleDTO(
        @JsonProperty("id")
        @field:Schema(description = "The unique identifier of the vehicle", defaultValue = "1")
        var id: Long? = null,

        @JsonProperty("vin")
        @field:Schema(description = "The Vehicle Identification Number", defaultValue = "JT6HF10U3Y0133607")
        var vin: String? = null,

        @JsonProperty("licensePlate")
        @field:Schema(description = "The license plate of the vehicle", defaultValue = "B-BB-1234")
        var licensePlate: String? = null,

        @JsonProperty("name")
        @field:Schema(description = "The name of the vehicle", defaultValue = "Aygoya")
        var name: String? = null,

        @JsonProperty("manufacturer")
        @field:Schema(description = "The manufacturer of the vehicle", defaultValue = "Toyota")
        var manufacturer: String? = null,

        @JsonProperty("model")
        @field:Schema(description = "The model of the vehicle", defaultValue = "Aygo")
        var model: String? = null
)
