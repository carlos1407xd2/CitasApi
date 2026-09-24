package co.edu.fcv.training.citas.application.catalog;

import java.util.List;

public interface InsurancePlanCatalogQuery {
    List<ActiveInsurancePlan> activePlans();

    record ActiveInsurancePlan(Long id, String name) {
    }
}
