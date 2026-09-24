package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "document_type", nullable = false)
    private String documentType;
    @Column(name = "document_number", nullable = false)
    private String documentNumber;
    @Column(nullable = false)
    private String email;
    private String phone;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    protected UserEntity() {
    }

    static UserEntity register(String firstName, String lastName, String documentType, String documentNumber,
            String email, String phone, String passwordHash, RoleEntity userRole) {
        UserEntity user = new UserEntity();
        user.firstName = firstName;
        user.lastName = lastName;
        user.documentType = documentType;
        user.documentNumber = documentNumber;
        user.email = email;
        user.phone = phone;
        user.passwordHash = passwordHash;
        user.roles.add(userRole);
        return user;
    }

    Long id() { return id; }
    String email() { return email; }
}
