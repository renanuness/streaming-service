package application.identity.service;

import identity.model.aggregate.User;
import org.springframework.beans.factory.annotation.Qualifier;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import identity.repository.UserRepository;
import identity.service.UserRegistrationDomainService;
import shared.exception.DuplicateResourceException;
import application.identity.dto.CreateUserRequest;
import application.identity.dto.UserResponse;

import org.springframework.stereotype.Service;

@Service("identityUserApplicationService")
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserRegistrationDomainService domainService;

    public UserApplicationService(@Qualifier("identityJpaUserRepository") UserRepository userRepository, UserRegistrationDomainService domainService) {
        this.userRepository = userRepository;
        this.domainService = domainService;
    }

    public UserResponse registerUser(CreateUserRequest request) {
        Email email = new Email(request.email());
        FullName name = new FullName(request.firstName(), request.lastName());

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email já cadastrado");
        }

        User user = domainService.register(
                name.firstName(),
                name.lastName(),
                email.value(),
                request.password(),
                request.dateOfBirth()
        );

        userRepository.save(user);

        return new UserResponse(
                user.id().value(),
                user.name().fullName(),
                user.email().value(),
                user.dateOfBirth().age()  ,
                user.status().description()
        );
    }
}
