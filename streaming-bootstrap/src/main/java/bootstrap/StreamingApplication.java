package bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "application",
        "infra",
        "interfaces",
        "subscription"
})
@EntityScan(basePackages = {
        "infra.core.entity",
        "infra.identity.entity",
        "infra.subscription.entity",
})
@EnableJpaRepositories(basePackages = {
        "infra.core.persistence",
        "infra.identity.persistence",
        "infra.subscription.persistence"
})
public class StreamingApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StreamingApplication.class, args);
    }
}
