package org.fakechitor.cloudfilestorage.model

import jakarta.persistence.*

@Entity
@Table(name = "user_roles")
class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_id_seq")
    @SequenceGenerator(name = "user_roles_id_seq", sequenceName = "user_roles_id_seq", allocationSize = 1)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    var user: User? = null

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    var role: Role? = null

    constructor(user: User?, role: Role?) {
        this.user = user
        this.role = role
    }
}
