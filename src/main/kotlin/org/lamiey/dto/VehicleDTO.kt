package org.lamiey.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VehicleDTO(
    @JsonProperty("id")
    var id: Long? = null,
    @JsonProperty("licensePlate")
    var licensePlate: String? = null,
    @JsonProperty("name")
    var name: String? = null,
    @JsonProperty("manufacturer")
    var manufacturer: String? = null,
    @JsonProperty("model")
    var model: String? = null
)
