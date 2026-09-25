package co.edu.fcv.training.citas.adapter.out.persistence.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "locations")
class LocationEntity {
    @Id private Short id;
    @Column(nullable = false) private String code;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String address;
    @Column(nullable = false) private String city;
    @Column(nullable = false) private String department;
    @Column(nullable = false) private boolean active;
    protected LocationEntity() {}
    Short id() { return id; }
    String code() { return code; }
    String name() { return name; }
    String address() { return address; }
    String city() { return city; }
    String department() { return department; }
}
