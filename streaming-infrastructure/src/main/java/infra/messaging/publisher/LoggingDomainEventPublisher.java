package infra.messaging.publisher;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import shared.domain.DomainEvent;
import shared.domain.DomainEventPublisher;

@Component
public class LoggingDomainEventPublisher implements DomainEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(LoggingDomainEventPublisher.class);

    @Override
    public void publish(DomainEvent event) {
        log.info("📢 Evento de domínio publicado:");
        log.info("   Tipo: {}", event.eventType());
        log.info("   ID: {}", event.eventId());
        log.info("   Ocorrido em: {}", event.occurredAt());
        log.info("   Dados: {}", event);
    }
}
