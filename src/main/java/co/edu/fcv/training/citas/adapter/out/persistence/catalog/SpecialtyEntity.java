package co.edu.fcv.training.citas.adapter.out.persistence.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "specialties")
class SpecialtyEntity {
    @Id private Short id;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String name;
    @Column(name = "appointment_duration_minutes", nullable = false) private Short appointmentDurationMinutes;
    @Column(name = "is_general", nullable = false) private boolean general;
    @Column(name = "requires_admin_approval", nullable = false) private boolean requiresAdminApproval;
    @Column(nullable = false) private boolean active;
    protected SpecialtyEntity() {}
    Short id() { return id; }
    String code() { return code; }
    String name() { return name; }
    int duration() { return appointmentDurationMinutes.intValue(); }
    boolean general() { return general; }
    boolean requiresAdminApproval() { return requiresAdminApproval; }
}
