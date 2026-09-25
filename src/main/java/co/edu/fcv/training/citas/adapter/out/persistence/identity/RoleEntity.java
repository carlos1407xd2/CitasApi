package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
class RoleEntity {

    @Id
    private Short id;

    private String code;

    protected RoleEntity() {
    }

    public String code() { return code; }
}
