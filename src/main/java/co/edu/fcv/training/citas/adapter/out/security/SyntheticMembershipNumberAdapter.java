package co.edu.fcv.training.citas.adapter.out.security;

import co.edu.fcv.training.citas.application.identity.MembershipNumberPort;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class SyntheticMembershipNumberAdapter implements MembershipNumberPort {

    @Override
    public String next() {
        return "AF-LAB-" + UUID.randomUUID();
    }
}
