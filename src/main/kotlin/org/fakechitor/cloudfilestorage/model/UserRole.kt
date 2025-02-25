package org.fakechitor.cloudfilestorage.model

import jakarta.persistence.*

@Entity
@Table(name = "user_roles")
class UserRole(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false) var user: User?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false) var role: Role?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_id_seq")
    @SequenceGenerator(name = "user_roles_id_seq", sequenceName = "user_roles_id_seq", allocationSize = 1)
    var id: Long? = null
}
