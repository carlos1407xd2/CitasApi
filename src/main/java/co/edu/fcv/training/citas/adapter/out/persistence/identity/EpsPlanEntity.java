package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eps_plans")
class EpsPlanEntity {

    @Id
    private Long id;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    private String name;

    protected EpsPlanEntity() {
    }

    boolean isActive() {
        return active;
    }

    Long id() {
        return id;
    }

    String name() {
        return name;
    }
}
