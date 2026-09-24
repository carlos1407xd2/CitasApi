package co.edu.fcv.training.citas.adapter.in.web.identity;

import co.edu.fcv.training.citas.application.identity.MembershipNumberPort;
import co.edu.fcv.training.citas.application.identity.PasswordHashPort;
import co.edu.fcv.training.citas.application.identity.RegisterUserService;
import co.edu.fcv.training.citas.application.identity.UserRegistrationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdentityApplicationConfiguration {

    @Bean
    RegisterUserService registerUserService(UserRegistrationPort userRegistrationPort, PasswordHashPort passwordHashPort,
            MembershipNumberPort membershipNumberPort) {
        return new RegisterUserService(userRegistrationPort, passwordHashPort, membershipNumberPort);
    }
}
