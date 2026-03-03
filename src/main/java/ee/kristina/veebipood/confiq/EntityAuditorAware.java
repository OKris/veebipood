package ee.kristina.veebipood.confiq;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class EntityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return Optional.of(email);
    }
}
