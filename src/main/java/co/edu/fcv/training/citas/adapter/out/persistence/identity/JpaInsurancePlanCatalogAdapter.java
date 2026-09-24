package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import co.edu.fcv.training.citas.application.catalog.InsurancePlanCatalogQuery;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class JpaInsurancePlanCatalogAdapter implements InsurancePlanCatalogQuery {

    private final EpsPlanJpaRepository plans;

    JpaInsurancePlanCatalogAdapter(EpsPlanJpaRepository plans) {
        this.plans = plans;
    }

    @Override
    public List<ActiveInsurancePlan> activePlans() {
        return plans.findAllByActiveTrueOrderByNameAsc().stream()
                .map(plan -> new ActiveInsurancePlan(plan.id(), plan.name()))
                .toList();
    }
}
