package co.edu.fcv.training.citas.adapter.in.web.catalog;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/catalogs")
class AdminCatalogController {
    private final EntityManager entityManager;
    AdminCatalogController(EntityManager entityManager) { this.entityManager = entityManager; }

    @GetMapping("/specialties")
    List<SpecialtyResponse> specialties() { return entityManager.createNativeQuery("SELECT id,code,name,appointment_duration_minutes,is_general,requires_admin_approval,active FROM specialties ORDER BY name").getResultList().stream().map(row -> { Object[] r=(Object[])row; return new SpecialtyResponse(((Number)r[0]).longValue(),(String)r[1],(String)r[2],((Number)r[3]).intValue(),(Boolean)r[4],(Boolean)r[5],(Boolean)r[6]); }).toList(); }

    @PostMapping("/specialties") @Transactional
    SpecialtyResponse createSpecialty(@Valid @RequestBody SpecialtyRequest request) { entityManager.createNativeQuery("INSERT INTO specialties(code,name,appointment_duration_minutes,is_general,requires_admin_approval,active) VALUES(:code,:name,:duration,:general,:approval,TRUE)").setParameter("code",request.code()).setParameter("name",request.name()).setParameter("duration",request.duration()).setParameter("general",request.general()).setParameter("approval",request.requiresAdminApproval()).executeUpdate(); Number id=(Number)entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult(); return new SpecialtyResponse(id.longValue(),request.code(),request.name(),request.duration(),request.general(),request.requiresAdminApproval(),true); }

    @PatchMapping("/specialties/{id}/active") @Transactional
    void setSpecialtyActive(@PathVariable Long id, @RequestBody ActiveRequest request) { entityManager.createNativeQuery("UPDATE specialties SET active=:active WHERE id=:id").setParameter("active",request.active()).setParameter("id",id).executeUpdate(); }
    @PutMapping("/specialties/{id}") @Transactional
    SpecialtyResponse updateSpecialty(@PathVariable Long id, @Valid @RequestBody SpecialtyRequest request) { entityManager.createNativeQuery("UPDATE specialties SET code=:code,name=:name,appointment_duration_minutes=:duration,is_general=:general,requires_admin_approval=:approval WHERE id=:id").setParameter("code",request.code()).setParameter("name",request.name()).setParameter("duration",request.duration()).setParameter("general",request.general()).setParameter("approval",request.requiresAdminApproval()).setParameter("id",id).executeUpdate(); return new SpecialtyResponse(id,request.code(),request.name(),request.duration(),request.general(),request.requiresAdminApproval(),true); }

    @GetMapping("/eps") List<EpsResponse> eps() { return entityManager.createNativeQuery("SELECT id,code,name,active FROM eps ORDER BY name").getResultList().stream().map(row -> { Object[] r=(Object[])row; return new EpsResponse(((Number)r[0]).longValue(),(String)r[1],(String)r[2],(Boolean)r[3]); }).toList(); }
    @PostMapping("/eps") @Transactional EpsResponse createEps(@Valid @RequestBody EpsRequest request) { entityManager.createNativeQuery("INSERT INTO eps(code,name,active) VALUES(:code,:name,TRUE)").setParameter("code",request.code()).setParameter("name",request.name()).executeUpdate(); Number id=(Number)entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult(); return new EpsResponse(id.longValue(),request.code(),request.name(),true); }
    @PatchMapping("/eps/{id}/active") @Transactional void setEpsActive(@PathVariable Long id,@RequestBody ActiveRequest request) { entityManager.createNativeQuery("UPDATE eps SET active=:active WHERE id=:id").setParameter("active",request.active()).setParameter("id",id).executeUpdate(); }
    @PutMapping("/eps/{id}") @Transactional EpsResponse updateEps(@PathVariable Long id,@Valid @RequestBody EpsRequest request) { entityManager.createNativeQuery("UPDATE eps SET code=:code,name=:name WHERE id=:id").setParameter("code",request.code()).setParameter("name",request.name()).setParameter("id",id).executeUpdate(); return new EpsResponse(id,request.code(),request.name(),true); }

    @GetMapping("/eps/{epsId}/plans") List<PlanResponse> plans(@PathVariable Long epsId) { return entityManager.createNativeQuery("SELECT id,eps_id,regime_id,code,name,active FROM eps_plans WHERE eps_id=:eps ORDER BY name").setParameter("eps",epsId).getResultList().stream().map(row -> { Object[] r=(Object[])row; return new PlanResponse(((Number)r[0]).longValue(),((Number)r[1]).longValue(),((Number)r[2]).longValue(),(String)r[3],(String)r[4],(Boolean)r[5]); }).toList(); }
    @PostMapping("/plans") @Transactional PlanResponse createPlan(@Valid @RequestBody PlanRequest request) { entityManager.createNativeQuery("INSERT INTO eps_plans(eps_id,regime_id,code,name,active) VALUES(:eps,:regime,:code,:name,TRUE)").setParameter("eps",request.epsId()).setParameter("regime",request.regimeId()).setParameter("code",request.code()).setParameter("name",request.name()).executeUpdate(); Number id=(Number)entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult(); return new PlanResponse(id.longValue(),request.epsId(),request.regimeId(),request.code(),request.name(),true); }
    @PatchMapping("/plans/{id}/active") @Transactional void setPlanActive(@PathVariable Long id,@RequestBody ActiveRequest request) { entityManager.createNativeQuery("UPDATE eps_plans SET active=:active WHERE id=:id").setParameter("active",request.active()).setParameter("id",id).executeUpdate(); }
    @PutMapping("/plans/{id}") @Transactional PlanResponse updatePlan(@PathVariable Long id,@Valid @RequestBody PlanRequest request) { entityManager.createNativeQuery("UPDATE eps_plans SET eps_id=:eps,regime_id=:regime,code=:code,name=:name WHERE id=:id").setParameter("eps",request.epsId()).setParameter("regime",request.regimeId()).setParameter("code",request.code()).setParameter("name",request.name()).setParameter("id",id).executeUpdate(); return new PlanResponse(id,request.epsId(),request.regimeId(),request.code(),request.name(),true); }

    record SpecialtyRequest(@NotBlank String code,@NotBlank String name,@NotNull Integer duration,boolean general,boolean requiresAdminApproval) {}
    record EpsRequest(@NotBlank String code,@NotBlank String name) {}
    record PlanRequest(@NotNull Long epsId,@NotNull Long regimeId,@NotBlank String code,@NotBlank String name) {}
    record ActiveRequest(boolean active) {}
    record SpecialtyResponse(Long id,String code,String name,Integer duration,Boolean general,Boolean requiresAdminApproval,Boolean active) {}
    record EpsResponse(Long id,String code,String name,Boolean active) {}
    record PlanResponse(Long id,Long epsId,Long regimeId,String code,String name,Boolean active) {}
}
