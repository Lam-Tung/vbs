package org.lamiey.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "booking")
class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    lateinit var vehicle: Vehicle

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @Column(name = "start_date", nullable = false) var startDate: LocalDateTime? = null

    @Column(name = "end_date", nullable = false) var endDate: LocalDateTime? = null

    @CreationTimestamp
    @Column(name = "created", updatable = false)
    var created: LocalDateTime? = null

    @UpdateTimestamp
    @Column(name = "updated")
    var updated: LocalDateTime? = null
}
