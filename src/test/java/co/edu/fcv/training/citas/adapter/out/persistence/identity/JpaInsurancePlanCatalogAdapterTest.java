package co.edu.fcv.training.citas.adapter.out.persistence.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JpaInsurancePlanCatalogAdapterTest {

    @Mock private EpsPlanJpaRepository plans;

    @Test
    void exposesOnlyTheRepositoryResultForActivePlans() {
        when(plans.findAllByActiveTrueOrderByNameAsc()).thenReturn(List.of(plan(3L, "Plan activo")));

        var result = new JpaInsurancePlanCatalogAdapter(plans).activePlans();

        assertThat(result).containsExactly(new co.edu.fcv.training.citas.application.catalog.InsurancePlanCatalogQuery.ActiveInsurancePlan(3L, "Plan activo"));
    }

    private EpsPlanEntity plan(Long id, String name) {
        var entity = new EpsPlanEntity();
        ReflectionTestUtils.setField(entity, "id", id);
        ReflectionTestUtils.setField(entity, "name", name);
        ReflectionTestUtils.setField(entity, "active", true);
        return entity;
    }
}
