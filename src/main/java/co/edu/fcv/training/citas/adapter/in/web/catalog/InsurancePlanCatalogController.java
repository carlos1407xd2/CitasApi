package co.edu.fcv.training.citas.adapter.in.web.catalog;

import co.edu.fcv.training.citas.application.catalog.InsurancePlanCatalogQuery;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalogs")
class InsurancePlanCatalogController {

    private final InsurancePlanCatalogQuery plans;

    InsurancePlanCatalogController(InsurancePlanCatalogQuery plans) {
        this.plans = plans;
    }

    @GetMapping("/insurance-plans")
    List<InsurancePlanResponse> activePlans() {
        return plans.activePlans().stream().map(plan -> new InsurancePlanResponse(plan.id(), plan.name())).toList();
    }

    record InsurancePlanResponse(Long id, String name) {
    }
}
