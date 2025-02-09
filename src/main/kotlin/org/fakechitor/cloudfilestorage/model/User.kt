package org.fakechitor.cloudfilestorage.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    var id: Long? = null

    @Column(unique = true)
    var login: String? = null
    var password: String? = null
}
