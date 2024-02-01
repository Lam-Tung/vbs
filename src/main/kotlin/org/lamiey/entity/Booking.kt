package org.lamiey.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "booking")
class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    lateinit var vehicle: Vehicle

    @ManyToOne
    @JoinColumn(name = "user_id")
    lateinit var user: User

    @Column(name = "start_date", nullable = false)
    lateinit var startDate: LocalDate

    @Column(name = "end_date", nullable = false)
    lateinit var endDate: LocalDate
}