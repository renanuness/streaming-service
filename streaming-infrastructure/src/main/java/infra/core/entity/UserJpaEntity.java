package infra.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="user_core")
@Data
public class UserJpaEntity {
    @Id
    private Long id;
}

