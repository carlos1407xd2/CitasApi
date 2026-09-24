package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_insurance_affiliations")
class UserInsuranceAffiliationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id")
    private EpsPlanEntity plan;
    @Column(name = "membership_number", nullable = false)
    private String membershipNumber;
    @Column(name = "is_current", nullable = false)
    private boolean current = true;

    protected UserInsuranceAffiliationEntity() {
    }

    static UserInsuranceAffiliationEntity current(UserEntity user, EpsPlanEntity plan, String membershipNumber) {
        UserInsuranceAffiliationEntity affiliation = new UserInsuranceAffiliationEntity();
        affiliation.user = user;
        affiliation.plan = plan;
        affiliation.membershipNumber = membershipNumber;
        return affiliation;
    }
}
