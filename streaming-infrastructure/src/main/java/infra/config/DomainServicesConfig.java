package infra.config;

import core.service.PlaylistDomainService;
import identity.service.UserRegistrationDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServicesConfig {

    @Bean
    public UserRegistrationDomainService userRegistrationDomainService() {
        return new UserRegistrationDomainService();
    }

    @Bean
    public PlaylistDomainService playlistDomainService() {
        return new PlaylistDomainService();
    }
}
