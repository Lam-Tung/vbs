package org.lamiey.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "\"user\"")
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null

    @Column(name = "email", nullable = false) var email: String? = null

    @Column(name = "first_name") var firstName: String? = null

    @Column(name = "last_name") var lastName: String? = null

    @CreationTimestamp
    @Column(name = "created", updatable = false)
    var created: LocalDateTime? = null

    @UpdateTimestamp @Column(name = "updated") var updated: LocalDateTime? = null
}
