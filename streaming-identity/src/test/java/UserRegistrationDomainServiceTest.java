import identity.model.aggregate.User;
import identity.service.UserRegistrationDomainService;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserRegistrationDomainServiceTest {
    private final UserRegistrationDomainService service = new UserRegistrationDomainService();

    @Test
    public void deveCadastrarUsuarioComDadosValidos() {
        User user = service.register(
                "João", "Silva",
                "joao.silva@email.com",
                "SenhaForte123",
                LocalDate.of(1990, 5, 15)
        );

        assertNotNull(user);
        assertEquals("João Silva", user.name().fullName());
        assertEquals("joao.silva@email.com", user.email().value());
        assertEquals(36, user.dateOfBirth().age());
    }
}
