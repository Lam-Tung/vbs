package org.lamiey.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.eclipse.microprofile.openapi.annotations.media.Schema

data class UserDTO(
    @JsonProperty("id")
    @field:Schema(description = "The unique identifier of the user", defaultValue = "1")
    var id: Long? = 0,

    @JsonProperty("email")
    @field:Schema(description = "The email address of the user", defaultValue = "user@example.com")
    var email: String? = "user@example.com",

    @JsonProperty("firstName")
    @field:Schema(description = "The first name of the user", defaultValue = "John")
    var firstName: String? = "John",

    @JsonProperty("lastName")
    @field:Schema(description = "The last name of the user", defaultValue = "Doe")
    var lastName: String? = "Doe"
)
