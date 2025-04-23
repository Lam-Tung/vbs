package org.lamiey.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "vehicle")
class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null

    @Column(name = "vin") lateinit var vin: String

    @Column(name = "license_plate") var licensePlate: String? = null

    @Column(name = "name") var name: String? = null

    @Column(name = "manufacturer") var manufacturer: String? = null

    @Column(name = "model") var model: String? = null

    @CreationTimestamp
    @Column(name = "created", updatable = false)
    var created: LocalDateTime? = null

    @UpdateTimestamp @Column(name = "updated") var updated: LocalDateTime? = null
}
