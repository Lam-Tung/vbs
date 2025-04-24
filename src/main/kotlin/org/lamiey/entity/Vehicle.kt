package org.lamiey.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "vehicle")
class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null

    @Column(name = "vin", nullable = false, unique = true) var vin: String? = null

    @Column(name = "license_plate") var licensePlate: String? = null

    @Column(name = "name") var name: String? = null

    @Column(name = "manufacturer") var manufacturer: String? = null

    @Column(name = "model") var model: String? = null

    @CreationTimestamp
    @Column(name = "created", updatable = false)
    var created: LocalDateTime? = null

    @UpdateTimestamp @Column(name = "updated") var updated: LocalDateTime? = null
}
