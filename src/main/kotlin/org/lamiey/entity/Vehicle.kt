package org.lamiey.entity

import jakarta.persistence.*

@Entity
@Table(name = "vehicle")
class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "license_plate")
    var licensePlate: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "manufacturer")
    var manufacturer: String? = null

    @Column(name = "model")
    var model: String? = null
}
